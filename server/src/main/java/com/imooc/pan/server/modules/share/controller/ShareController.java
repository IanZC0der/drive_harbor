package com.imooc.pan.server.modules.share.controller;

import com.google.common.base.Splitter;
import com.imooc.pan.core.constants.driveHarborConstants;
import com.imooc.pan.core.response.R;
import com.imooc.pan.core.utils.IdUtil;
import com.imooc.pan.server.common.annotation.LoginIgnore;
import com.imooc.pan.server.common.annotation.NeedShareCode;
import com.imooc.pan.server.common.utils.ShareIdUtil;
import com.imooc.pan.server.common.utils.UserIdUtil;
import com.imooc.pan.server.modules.recycle.context.QueryChildFileListContext;
import com.imooc.pan.server.modules.file.vo.DriveHarborUserFileVO;
import com.imooc.pan.server.modules.recycle.context.ShareSaveContext;
import com.imooc.pan.server.modules.share.context.*;
import com.imooc.pan.server.modules.share.converter.ShareConverter;
import com.imooc.pan.server.modules.share.po.CancelSharePO;
import com.imooc.pan.server.modules.share.po.CreateShareUrlPO;
import com.imooc.pan.server.modules.share.po.CheckShareCodePO;
import com.imooc.pan.server.modules.share.po.ShareSavePO;
import com.imooc.pan.server.modules.share.service.IShareService;
import com.imooc.pan.server.modules.share.vo.DriveHarborShareUrlListVO;
import com.imooc.pan.server.modules.share.vo.DriveHarborShareUrlVO;
import com.imooc.pan.server.modules.share.vo.ShareDetailVO;
import com.imooc.pan.server.modules.share.vo.ShareSimpleDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Share module")
@RestController
@Validated
public class ShareController {
    @Autowired
    private IShareService iShareService;

    @Autowired
    private ShareConverter shareConverter;

    @ApiOperation(
            value = "create share link",
            notes = "This interface provides the funtionality of creating share link",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("share")
    public R<DriveHarborShareUrlVO> create(@Validated @RequestBody CreateShareUrlPO createShareUrlPO) {
        CreateShareUrlContext context = shareConverter.createShareUrlPO2CreateShareUrlContext(createShareUrlPO);

        String shareFileIds = createShareUrlPO.getShareFileIds();
        List<Long> shareFileIdList = Splitter.on(driveHarborConstants.COMMON_SEPARATOR).splitToList(shareFileIds).stream().map(IdUtil::decrypt).collect(Collectors.toList());

        context.setShareFileIdList(shareFileIdList);

        DriveHarborShareUrlVO vo = iShareService.create(context);
        return R.data(vo);
    }


    @ApiOperation(
            value = "query the share links that have been created",
            notes = "This interface provides the functionality of querying the share links",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("shares")
    public R<List<DriveHarborShareUrlListVO>> getShares() {
        QueryShareListContext context = new QueryShareListContext();
        context.setUserId(UserIdUtil.get());
        List<DriveHarborShareUrlListVO> result = iShareService.getShares(context);
        return R.data(result);
    }

    @ApiOperation(
            value = "cancel share",
            notes = "This interface provides the functionality of cancelling the created share ",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @DeleteMapping("share")
    public R cancelShare(@Validated @RequestBody CancelSharePO cancelSharePO) {
        CancelShareContext context = new CancelShareContext();

        context.setUserId(UserIdUtil.get());

        String shareIds = cancelSharePO.getShareIds();
        List<Long> shareIdList = Splitter.on(driveHarborConstants.COMMON_SEPARATOR).splitToList(shareIds).stream().map(IdUtil::decrypt).collect(Collectors.toList());
        context.setShareIdList(shareIdList);

        iShareService.cancelShare(context);
        return R.success();
    }

    @ApiOperation(
            value = "verify the share code",
            notes = "this interface provides the functionality of verifying the share code",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @LoginIgnore
    @PostMapping("share/code/check")
    public R<String> checkShareCode(@Validated @RequestBody CheckShareCodePO checkShareCodePO) {
        CheckShareCodeContext context = new CheckShareCodeContext();

        context.setShareId(IdUtil.decrypt(checkShareCodePO.getShareId()));
        context.setShareCode(checkShareCodePO.getShareCode());

        String token = iShareService.checkShareCode(context);
        return R.data(token);
    }
    @ApiOperation(
            value = "Query share details",
            notes = "This interface provides the functionality of querying the share details",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @LoginIgnore
    @NeedShareCode
    @GetMapping("share")
    public R<ShareDetailVO> detail() {
        QueryShareDetailContext context = new QueryShareDetailContext();
        context.setShareId(ShareIdUtil.get());
        ShareDetailVO vo = iShareService.detail(context);
        return R.data(vo);
    }

    @ApiOperation(
            value = "query simplified share info",
            notes = "This interface provides the functionality of simplified share info",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @LoginIgnore
    @GetMapping("share/simple")
    public R<ShareSimpleDetailVO> simpleDetail(@NotBlank(message = "share id cannot be null") @RequestParam(value = "shareId", required = false) String shareId) {
        QueryShareSimpleDetailContext context = new QueryShareSimpleDetailContext();
        context.setShareId(IdUtil.decrypt(shareId));
        ShareSimpleDetailVO vo = iShareService.simpleDetail(context);
        return R.data(vo);
    }

    @ApiOperation(
            value = "Get file list in current directory in the current share",
            notes = "This interface is for querying the file list in the current directory in the current share",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("share/file/list")
    @NeedShareCode
    @LoginIgnore
    public R<List<DriveHarborUserFileVO>> fileList(@NotBlank(message = "parent id cannot be null") @RequestParam(value = "parentId", required = false) String parentId) {
        QueryChildFileListContext context = new QueryChildFileListContext();
        context.setShareId(ShareIdUtil.get());
        context.setParentId(IdUtil.decrypt(parentId));
        List<DriveHarborUserFileVO> result = iShareService.fileList(context);
        return R.data(result);
    }

    @ApiOperation(
            value = "save the shared files to my drive",
            notes = "This interface provides the functionality of saving the files to my own drive",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @NeedShareCode
    @PostMapping("share/save")
    public R saveFiles(@Validated @RequestBody ShareSavePO shareSavePO) {
        ShareSaveContext context = new ShareSaveContext();

        String fileIds = shareSavePO.getFileIds();
        List<Long> fileIdList = Splitter.on(driveHarborConstants.COMMON_SEPARATOR).splitToList(fileIds).stream().map(IdUtil::decrypt).collect(Collectors.toList());
        context.setFileIdList(fileIdList);

        context.setTargetParentId(IdUtil.decrypt(shareSavePO.getTargetParentId()));
        context.setUserId(UserIdUtil.get());
        context.setShareId(ShareIdUtil.get());

        iShareService.saveFiles(context);
        return R.success();
    }

    @ApiOperation(
            value = "download the files shared",
            notes = "This interface is for downloading the files in the share",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("share/file/download")
    @NeedShareCode
    public void download(@NotBlank(message = "fileId cannot be null") @RequestParam(value = "fileId", required = false) String fileId,
                         HttpServletResponse response) {
        ShareFileDownloadContext context = new ShareFileDownloadContext();
        context.setFileId(IdUtil.decrypt(fileId));
        context.setShareId(ShareIdUtil.get());
        context.setUserId(UserIdUtil.get());
        context.setResponse(response);
        iShareService.download(context);
    }
}
