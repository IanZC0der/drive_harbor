package com.imooc.pan.server.modules.share.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.imooc.pan.bloom.filter.core.BloomFilter;
import com.imooc.pan.bloom.filter.core.BloomFilterManager;
import com.imooc.pan.core.constants.driveHarborConstants;
import com.imooc.pan.core.exception.driveHarborBusinessException;
import com.imooc.pan.core.response.ResponseCode;
import com.imooc.pan.core.utils.IdUtil;
import com.imooc.pan.core.utils.JwtUtil;
import com.imooc.pan.core.utils.UUIDUtil;
import com.imooc.pan.server.common.cache.ManualCacheService;
import com.imooc.pan.server.common.config.HarborServerConfig;
import com.imooc.pan.server.common.stream.channel.DriveHarborChannels;
import com.imooc.pan.server.common.stream.event.log.ErrorLogEvent;
import com.imooc.pan.server.modules.file.constants.FileConstants;
import com.imooc.pan.server.modules.file.context.CopyFileContext;
import com.imooc.pan.server.modules.file.context.FileDownloadContext;
import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import com.imooc.pan.server.modules.recycle.context.QueryChildFileListContext;
import com.imooc.pan.server.modules.file.context.QueryFileListContext;
import com.imooc.pan.server.modules.file.enums.DelFlagEnum;
import com.imooc.pan.server.modules.file.service.IUserFileService;
import com.imooc.pan.server.modules.file.vo.DriveHarborUserFileVO;
import com.imooc.pan.server.modules.recycle.context.ShareSaveContext;
import com.imooc.pan.server.modules.share.constants.ShareConstants;
import com.imooc.pan.server.modules.share.context.*;
import com.imooc.pan.server.modules.share.entity.driveHarborShare;
import com.imooc.pan.server.modules.share.enums.ShareDayTypeEnum;
import com.imooc.pan.server.modules.share.enums.ShareStatusEnum;
import com.imooc.pan.server.modules.share.service.IShareFileService;
import com.imooc.pan.server.modules.share.service.IShareService;
import com.imooc.pan.server.modules.share.mapper.driveHarborShareMapper;
import com.imooc.pan.server.modules.share.vo.*;
import com.imooc.pan.server.modules.user.entity.driveHarborUser;
import com.imooc.pan.server.modules.user.service.IUserService;
import com.imooc.pan.stream.core.IStreamProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author benchi
* @description 针对表【r_pan_share(User_Sharing_History_Table)】的数据库操作Service实现
* @createDate 2023-10-23 21:08:08
*/
@Service
@Slf4j
public class ShareServiceImpl extends ServiceImpl<driveHarborShareMapper, driveHarborShare>
    implements IShareService{
    @Autowired
    private HarborServerConfig config;

    @Autowired
    private IShareFileService iShareFileService;

    @Autowired
    private IUserService iUserService;


    @Autowired
    private IUserFileService iUserFileService;

    @Autowired
    @Qualifier(value = "shareManualCacheService")
    private ManualCacheService<driveHarborShare> cacheService;

    @Autowired
    private BloomFilterManager manager;

    @Autowired
    @Qualifier(value = "defaultStreamProducer")
    private IStreamProducer producer;



    private static final String BLOOM_FILTER_NAME = "SHARE_SIMPLE_DETAIL";



    /**
     * create share link
     * 1. assemble share entity and save it to the database
     * 2. save the share and user info
     * 3. return
     * @param context
     * @return
     */
    @Transactional(rollbackFor = driveHarborBusinessException.class)
    @Override
    public DriveHarborShareUrlVO create(CreateShareUrlContext context) {
        saveShare(context);
        saveShareFiles(context);
        DriveHarborShareUrlVO vo = assembleShareVO(context);
        afterCreate(context, vo);
        return vo;

    }


    /**
     * query share list
     * @param context
     * @return
     */
    @Override
    public List<DriveHarborShareUrlListVO> getShares(QueryShareListContext context) {
        return baseMapper.selectShareVOListByUserId(context.getUserId());
    }

    /**
     *
     * cancel the share link created
     * 1. check the permissions
     * 2. delete the share records
     * 3. delete the share records in regards to the files
     *
     * @param context
     */
    @Transactional(rollbackFor = driveHarborBusinessException.class)
    @Override
    public void cancelShare(CancelShareContext context) {
        checkUserCancelSharePermission(context);
        doCancelShare(context);
        doCancelShareFiles(context);

    }

    /**
     * check the share code
     * 1. check share status
     * 2. check share code
     * 3. generate share token
     * @param context
     * @return
     */
    @Override
    public String checkShareCode(CheckShareCodeContext context) {
        driveHarborShare record = checkShareStatus(context.getShareId());
        context.setRecord(record);
        doCheckShareCode(context);
        return generateShareToken(context);

    }

    /**
     * 1. check share status
     * 2. initialize share vo
     * 3. query share info
     * 4. query files shared
     * 5. query sharer info
     * @param context
     * @return
     */
    @Override
    public ShareDetailVO detail(QueryShareDetailContext context) {
        driveHarborShare record = checkShareStatus(context.getShareId());
        context.setRecord(record);
        initShareVO(context);
        assembleMainShareInfo(context);
        assembleShareFilesInfo(context);
        assembleShareUserInfo(context);
        return context.getVo();
    }

    /**
     *
     * 1. check share status
     * 2. initialize share vo
     * 3. query share info
     * 4. query sharer info
     * @param context
     * @return
     */
    @Override
    public ShareSimpleDetailVO simpleDetail(QueryShareSimpleDetailContext context) {
        driveHarborShare record = checkShareStatus(context.getShareId());
        context.setRecord(record);
        initShareSimpleVO(context);
        assembleMainShareSimpleInfo(context);
        assembleShareSimpleUserInfo(context);
        return context.getVo();
    }

    /**
     * 1. check share status
     * 2. verify that the parent id of the file is among the shared file list
     * 3. get the children
     * @param context
     * @return
     */
    @Override
    public List<DriveHarborUserFileVO> fileList(QueryChildFileListContext context) {
        driveHarborShare record = checkShareStatus(context.getShareId());
        context.setRecord(record);
        List<DriveHarborUserFileVO> allUserFileRecords = checkFileIdIsOnShareStatusAndGetAllShareUserFiles(context.getShareId(), Lists.newArrayList(context.getParentId()));
        Map<Long, List<DriveHarborUserFileVO>> parentIdFileListMap = allUserFileRecords.stream().collect(Collectors.groupingBy(DriveHarborUserFileVO::getParentId));
        List<DriveHarborUserFileVO> driveHarborUserFileVOS = parentIdFileListMap.get(context.getParentId());
        if (CollectionUtils.isEmpty(driveHarborUserFileVOS)) {
            return Lists.newArrayList();
        }
        return driveHarborUserFileVOS;

    }

    /**
     * 1, check share status
     * 2. check file ids on the shared files list
     * 3. save
     * @param context
     */
    @Override
    public void saveFiles(ShareSaveContext context) {
        checkShareStatus(context.getShareId());
        checkFileIdIsOnShareStatus(context.getShareId(), context.getFileIdList());
        doSaveFiles(context);
    }

    @Override
    public void download(ShareFileDownloadContext context) {
        checkShareStatus(context.getShareId());
        checkFileIdIsOnShareStatus(context.getShareId(), Lists.newArrayList(context.getFileId()));
        doDownload(context);
    }

    /**
     * 1. get all the share records affected by the events
     * 2. check if the file and its parent are normal, change the share status to normal if its true
     * 3. if the file in the share or the parent is deleted, update the share status to deleted
     * @param allAvailableFileIdList
     */
    @Override
    public void refreshShareStatus(List<Long> allAvailableFileIdList) {
        List<Long> shareIdList = getShareIdListByFileIdList(allAvailableFileIdList);
        if (CollectionUtils.isEmpty(shareIdList)) {
            return;
        }
        Set<Long> shareIdSet = Sets.newHashSet(shareIdList);
        shareIdSet.stream().forEach(this::refreshOneShareStatus);

    }

    @Override
    public List<Long> rollingQueryShareId(long startId, long limit) {

        return baseMapper.rollingQueryShareId(startId, limit);

    }

    @Override
    public boolean removeById(Serializable id) {
        return cacheService.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return cacheService.removeByIds(idList);
    }

    @Override
    public boolean updateById(driveHarborShare entity) {
        return cacheService.updateById(entity.getShareId(), entity);
    }

    @Override
    public boolean updateBatchById(Collection<driveHarborShare> entityList) {
        if(CollectionUtils.isEmpty(entityList)){
            return true;
        }
        Map<Long, driveHarborShare> entityMap = entityList.stream().collect(Collectors.toMap(driveHarborShare::getShareId, e -> e));
        return cacheService.updateByIds(entityMap);
    }

    @Override
    public driveHarborShare getById(Serializable id) {
//        return super.getById(id);
        return cacheService.getById(id);
    }

    @Override
    public List<driveHarborShare> listByIds(Collection<? extends Serializable> idList) {
//        return super.listByIds(idList);
        return cacheService.getByIds(idList);
    }
    private void afterCreate(CreateShareUrlContext context, DriveHarborShareUrlVO vo) {
        BloomFilter<Long> bloomFilter = manager.getFilter(BLOOM_FILTER_NAME);
        if (Objects.nonNull(bloomFilter)) {
            bloomFilter.put(context.getRecord().getShareId());
            log.info("created share, added share id to bloom filter, share id is {}", context.getRecord().getShareId());
        }

    }

    private void refreshOneShareStatus(Long shareId) {
        driveHarborShare record = getById(shareId);
        if (Objects.isNull(record)) {
            return;
        }

        ShareStatusEnum shareStatus = ShareStatusEnum.NORMAL;
        if (!checkShareFileAvailable(shareId)) {
            shareStatus = ShareStatusEnum.FILE_DELETED;
        }

        if (Objects.equals(record.getShareStatus(), shareStatus.getCode())) {
            return;
        }

        doChangeShareStatus(record, shareStatus);
    }

    private void doChangeShareStatus(driveHarborShare record, ShareStatusEnum shareStatus) {
        record.setShareStatus(record.getShareStatus());
        if(!updateById(record)){
            producer.sendMessage(DriveHarborChannels.ERROR_LOG_OUTPUT,new ErrorLogEvent("Update share status failure, share ID: " + record.getShareId() + ", share status to be changed to: "+shareStatus.getCode(), driveHarborConstants.ZERO_LONG));

        }



    }

    private boolean checkShareFileAvailable(Long shareId) {
        List<Long> shareFileIdList = getShareFileIdList(shareId);
        for (Long fileId : shareFileIdList) {
            if (!checkUpFileAvailable(fileId)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkUpFileAvailable(Long fileId) {
        driveHarborUserFile record = iUserFileService.getById(fileId);
        if (Objects.isNull(record)) {
            return false;
        }
        if (Objects.equals(record.getDelFlag(), DelFlagEnum.YES.getCode())) {
            return false;
        }
        if (Objects.equals(record.getParentId(), FileConstants.TOP_PARENT_ID)) {
            return true;
        }
        return checkUpFileAvailable(record.getParentId());
    }

    private List<Long> getShareIdListByFileIdList(List<Long> allAvailableFileIdList) {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.select("share_id");
        queryWrapper.in("file_id", allAvailableFileIdList);
        List<Long> shareIdList = iShareFileService.listObjs(queryWrapper, value -> (Long) value);
        return shareIdList;
    }

    private void doDownload(ShareFileDownloadContext context) {
        FileDownloadContext fileDownloadContext = new FileDownloadContext();
        fileDownloadContext.setFileId(context.getFileId());
        fileDownloadContext.setUserId(context.getUserId());
        fileDownloadContext.setResponse(context.getResponse());
        iUserFileService.downloadWithoutCheckUser(fileDownloadContext);
    }

    private void doSaveFiles(ShareSaveContext context) {
        CopyFileContext copyFileContext = new CopyFileContext();
        copyFileContext.setFileIdList(context.getFileIdList());
        copyFileContext.setTargetParentId(context.getTargetParentId());
        copyFileContext.setUserId(context.getUserId());
        iUserFileService.copy(copyFileContext);
    }

    private void checkFileIdIsOnShareStatus(Long shareId, List<Long> fileIdList) {
        checkFileIdIsOnShareStatusAndGetAllShareUserFiles(shareId, fileIdList);
    }

    /**
     * check share status
     * return all the files in the share
     * @param shareId
     * @param fileIdList
     * @return
     */
    private List<DriveHarborUserFileVO> checkFileIdIsOnShareStatusAndGetAllShareUserFiles(Long shareId, List<Long> fileIdList) {
        List<Long> shareFileIdList = getShareFileIdList(shareId);
        if (CollectionUtils.isEmpty(shareFileIdList)) {
            return Lists.newArrayList();
        }
        List<driveHarborUserFile> allFileRecords = iUserFileService.findAllFileRecordsByFileIdList(shareFileIdList);
        if (CollectionUtils.isEmpty(allFileRecords)) {
            return Lists.newArrayList();
        }
        allFileRecords = allFileRecords.stream()
                .filter(Objects::nonNull)
                .filter(record -> Objects.equals(record.getDelFlag(), DelFlagEnum.NO.getCode()))
                .collect(Collectors.toList());

        List<Long> allFileIdList = allFileRecords.stream().map(driveHarborUserFile::getFileId).collect(Collectors.toList());

        if (allFileIdList.containsAll(fileIdList)) {
            return iUserFileService.transferVOList(allFileRecords);
        }

        throw new driveHarborBusinessException(ResponseCode.SHARE_FILE_MISS);
    }

    private void assembleShareSimpleUserInfo(QueryShareSimpleDetailContext context) {
        driveHarborUser record = iUserService.getById(context.getRecord().getCreateUser());
        if (Objects.isNull(record)) {
            throw new driveHarborBusinessException("query user info failure");
        }
        ShareUserInfoVO shareUserInfoVO = new ShareUserInfoVO();

        shareUserInfoVO.setUserId(record.getUserId());
        shareUserInfoVO.setUsername(encryptUsername(record.getUsername()));

        context.getVo().setShareUserInfoVO(shareUserInfoVO);


    }

    private void assembleMainShareSimpleInfo(QueryShareSimpleDetailContext context) {
        driveHarborShare record = context.getRecord();
        ShareSimpleDetailVO vo = context.getVo();
        vo.setShareId(record.getShareId());
        vo.setShareName(record.getShareName());

    }

    private void initShareSimpleVO(QueryShareSimpleDetailContext context) {
        ShareSimpleDetailVO vo = new ShareSimpleDetailVO();
        context.setVo(vo);

    }

    /**
     * get sharer info and save it to the context
     * @param context
     */
    private void assembleShareUserInfo(QueryShareDetailContext context) {

        driveHarborUser record = iUserService.getById(context.getRecord().getCreateUser());
        if (Objects.isNull(record)) {
            throw new driveHarborBusinessException("User info query failure");
        }
        ShareUserInfoVO shareUserInfoVO = new ShareUserInfoVO();

        shareUserInfoVO.setUserId(record.getUserId());
        shareUserInfoVO.setUsername(encryptUsername(record.getUsername()));

        context.getVo().setShareUserInfoVO(shareUserInfoVO);

    }

    /**
     * encrypt user name
     * @param username
     * @return
     */
    private String encryptUsername(String username) {
        StringBuffer stringBuffer = new StringBuffer(username);
        stringBuffer.replace(driveHarborConstants.TWO_INT, username.length() - driveHarborConstants.TWO_INT, driveHarborConstants.COMMON_ENCRYPT_STR);
        return stringBuffer.toString();
    }

    /**
     * get share info
     * @param context
     */
    private void assembleMainShareInfo(QueryShareDetailContext context) {
        driveHarborShare record = context.getRecord();
        ShareDetailVO vo = context.getVo();
        vo.setShareId(record.getShareId());
        vo.setShareName(record.getShareName());
        vo.setCreateTime(record.getCreateTime());
        vo.setShareDay(record.getShareDay());
        vo.setShareEndTime(record.getShareEndTime());

    }

    /**
     * get share files id list
     * set the info in the context
     * @param context
     */
    private void assembleShareFilesInfo(QueryShareDetailContext context) {
        List<Long> fileIdList = getShareFileIdList(context.getShareId());

        QueryFileListContext queryFileListContext = new QueryFileListContext();
        queryFileListContext.setUserId(context.getRecord().getCreateUser());
        queryFileListContext.setDelFlag(DelFlagEnum.NO.getCode());
        queryFileListContext.setFileIdList(fileIdList);

        List<DriveHarborUserFileVO> DriveHarborUserFileVOList = iUserFileService.getFileList(queryFileListContext);
        context.getVo().setDriveHarborUserFileVOList(DriveHarborUserFileVOList);

    }

    /**
     * get share file ids list
     *
     * @param shareId
     * @return
     */
    private List<Long> getShareFileIdList(Long shareId) {
        if (Objects.isNull(shareId)) {
            return Lists.newArrayList();
        }
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.select("file_id");
        queryWrapper.eq("share_id", shareId);
        List<Long> fileIdList = iShareFileService.listObjs(queryWrapper, value -> (Long) value);
        return fileIdList;
    }

    private void initShareVO(QueryShareDetailContext context) {
        ShareDetailVO vo = new ShareDetailVO();
        context.setVo(vo);
    }

    /***************************************private***************************************/

    /**
     * generate share token
     *
     * @param context
     * @return
     */
    private String generateShareToken(CheckShareCodeContext context) {
        driveHarborShare record = context.getRecord();
        String token = JwtUtil.generateToken(UUIDUtil.getUUID(), ShareConstants.SHARE_ID, record.getShareId(), ShareConstants.ONE_HOUR_LONG);
        return token;
    }
    /**
     * check share code
     *
     * @param context
     */
    private void doCheckShareCode(CheckShareCodeContext context) {
        driveHarborShare record = context.getRecord();
        if (!Objects.equals(context.getShareCode(), record.getShareCode())) {
            throw new driveHarborBusinessException("Wrong code");
        }
    }

    /**
     * check share status
     *
     * @param shareId
     * @return
     */
    private driveHarborShare checkShareStatus(Long shareId) {
        driveHarborShare record = getById(shareId);

        if (Objects.isNull(record)) {
            throw new driveHarborBusinessException(ResponseCode.SHARE_CANCELLED);
        }

        if (Objects.equals(ShareStatusEnum.FILE_DELETED.getCode(), record.getShareStatus())) {
            throw new driveHarborBusinessException(ResponseCode.SHARE_FILE_MISS);
        }

        if (Objects.equals(ShareDayTypeEnum.PERMANENT_VALIDITY.getCode(), record.getShareDayType())) {
            return record;
        }

        if (record.getShareEndTime().before(new Date())) {
            throw new driveHarborBusinessException(ResponseCode.SHARE_EXPIRE);
        }

        return record;
    }

    /**
     * assemble share vo
     * @param context
     * @return
     */
    private DriveHarborShareUrlVO assembleShareVO(CreateShareUrlContext context) {
        driveHarborShare record = context.getRecord();
        DriveHarborShareUrlVO vo = new DriveHarborShareUrlVO();
        vo.setShareId(record.getShareId());
        vo.setShareName(record.getShareName());
        vo.setShareUrl(record.getShareUrl());
        vo.setShareCode(record.getShareCode());
        vo.setShareStatus(record.getShareStatus());
        return vo;
    }

    private void saveShareFiles(CreateShareUrlContext context) {
        SaveShareFilesContext saveShareFilesContext = new SaveShareFilesContext();
        saveShareFilesContext.setShareId(context.getRecord().getShareId());
        saveShareFilesContext.setShareFileIdList(context.getShareFileIdList());
        saveShareFilesContext.setUserId(context.getUserId());
        iShareFileService.saveShareFiles(saveShareFilesContext);


    }

    private void saveShare(CreateShareUrlContext context) {
        driveHarborShare record = new driveHarborShare();

        record.setShareId(IdUtil.get());
        record.setShareName(context.getShareName());
        record.setShareType(context.getShareType());
        record.setShareDayType(context.getShareDayType());

        Integer shareDay = ShareDayTypeEnum.getShareDayByCode(context.getShareDayType());
        if (Objects.equals(driveHarborConstants.MINUS_ONE_INT, shareDay)) {
            throw new driveHarborBusinessException("Invalid share time");
        }

        record.setShareDay(shareDay);
        record.setShareEndTime(DateUtil.offsetDay(new Date(), shareDay));
        record.setShareUrl(createShareUrl(record.getShareId()));
        record.setShareCode(createShareCode());
        record.setShareStatus(ShareStatusEnum.NORMAL.getCode());
        record.setCreateUser(context.getUserId());
        record.setCreateTime(new Date());

        if (!save(record)) {
            throw new driveHarborBusinessException("Save share info failure");
        }

        context.setRecord(record);

    }

    /**
     * create share code
     * password to the share
     * @return
     */
    private String createShareCode() {
        return RandomStringUtils.randomAlphabetic(4).toLowerCase();

    }

    /**
     * create share url
     * @param shareId
     * @return
     */
    private String createShareUrl(Long shareId) {
        if (Objects.isNull(shareId)) {
            throw new driveHarborBusinessException("share ID cannot be null");
        }
        String sharePrefix = config.getSharePrefix();
        if (!sharePrefix.endsWith(driveHarborConstants.SLASH_STR)) {
            sharePrefix += driveHarborConstants.SLASH_STR;
        }
        return sharePrefix + URLEncoder.encode(IdUtil.encrypt(shareId));
    }

    /**
     * share if the current user has the permission to delete the share record
     *
     * @param context
     */
    private void checkUserCancelSharePermission(CancelShareContext context) {
        List<Long> shareIdList = context.getShareIdList();
        Long userId = context.getUserId();
        List<driveHarborShare> records = listByIds(shareIdList);
        if (CollectionUtils.isEmpty(records)) {
            throw new driveHarborBusinessException("No permission.");
        }
        for (driveHarborShare record : records) {
            if (!Objects.equals(userId, record.getCreateUser())) {
                throw new driveHarborBusinessException("No permission");
            }
        }
    }

    /**
     * cancel the share file records
     *
     * @param context
     */
    private void doCancelShareFiles(CancelShareContext context) {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.in("share_id", context.getShareIdList());
        queryWrapper.eq("create_user", context.getUserId());
        if (!iShareFileService.remove(queryWrapper)) {
            throw new driveHarborBusinessException("cancel failure.");
        }
    }

    /**
     * cancel the share
     *
     * @param context
     */
    private void doCancelShare(CancelShareContext context) {
        List<Long> shareIdList = context.getShareIdList();
        if (!removeByIds(shareIdList)) {
            throw new driveHarborBusinessException("Cancel failure.");
        }
    }


}




