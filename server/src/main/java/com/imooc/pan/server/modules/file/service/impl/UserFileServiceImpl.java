package com.imooc.pan.server.modules.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.pan.core.constants.driveHarborConstants;
import com.imooc.pan.core.exception.driveHarborBusinessException;
import com.imooc.pan.server.modules.file.context.CreateFolderContext;
import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import com.imooc.pan.server.modules.file.enums.DelFlagEnum;
import com.imooc.pan.server.modules.file.enums.FolderFlagEnum;
import com.imooc.pan.server.modules.file.service.IUserFileService;
import com.imooc.pan.server.modules.file.mapper.driverHarborUserFileMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.imooc.pan.core.utils.IdUtil;
import com.imooc.pan.server.modules.file.constants.FileConstants;

import java.util.Date;

/**
* @author benchi
* @description 针对表【r_pan_user_file(user file information table)】的数据库操作Service实现
* @createDate 2023-10-23 21:04:43
*/
@Service(value = "userFileService")
public class UserFileServiceImpl extends ServiceImpl<driverHarborUserFileMapper, driveHarborUserFile> implements IUserFileService {

    @Override
    public Long createFolder(CreateFolderContext createFolderContext) {
        return saveUserFile(createFolderContext.getParentId(),
                createFolderContext.getFolderName(),
                FolderFlagEnum.YES,
                null,
                null,
                createFolderContext.getUserId(),
                null);
    }
    private Long saveUserFile(Long parentId,
                              String fileName,
                              FolderFlagEnum folderFlagEnum,
                              Integer fileType,
                              Long realFileId,
                              Long userId,
                              String fileSizeDesc){
        driveHarborUserFile entity = assembleDriveHarborUserFile(parentId, userId, fileName, folderFlagEnum, fileType, realFileId, fileSizeDesc);
        if (!save((entity))) {
            throw new driveHarborBusinessException("Failed to save file info");
        }
        return entity.getFileId();

    }

    private driveHarborUserFile assembleDriveHarborUserFile(Long parentId, Long userId, String fileName, FolderFlagEnum folderFlagEnum, Integer fileType, Long realFileId, String fileSizeDesc) {
        driveHarborUserFile entity = new driveHarborUserFile();

        entity.setFileId(IdUtil.get());
        entity.setUserId(userId);
        entity.setParentId(parentId);
        entity.setRealFileId(realFileId);
        entity.setFileName(fileName);
        entity.setFolderFlag(folderFlagEnum.getCode());
        entity.setFileSizeDesc(fileSizeDesc);
        entity.setFileType(fileType);
        entity.setDelFlag(DelFlagEnum.NO.getCode());
        entity.setCreateUser(userId);
        entity.setCreateTime(new Date());
        entity.setUpdateUser(userId);
        entity.setUpdateTime(new Date());

        handleDuplicateFilename(entity);

        return entity;
    }

    /**
     * handle duplicate files names
     * @param entity
     */
    private void handleDuplicateFilename(driveHarborUserFile entity) {
        String filename = entity.getFileName(),
                newFilenameWithoutSuffix,
                newFilenameSuffix;
        int newFilenamePointPosition = filename.lastIndexOf(driveHarborConstants.POINT_STR);
        if (newFilenamePointPosition == driveHarborConstants.MINUS_ONE_INT) {
            newFilenameWithoutSuffix = filename;
            newFilenameSuffix = StringUtils.EMPTY;
        } else {
            newFilenameWithoutSuffix = filename.substring(driveHarborConstants.ZERO_INT, newFilenamePointPosition);
            newFilenameSuffix = filename.replace(newFilenameWithoutSuffix, StringUtils.EMPTY);
        }

        int count = getDuplicateFilename(entity, newFilenameWithoutSuffix);
        if(count == 0) {
            return;
        }
        String newFilename = assembleNewFileName(newFilenameWithoutSuffix, count, newFilenameSuffix);
        entity.setFileName(newFilename);
    }

    /**
     * aeemble new file name.
     * the duplicated file should keep the duplicated name with suffix like "(1)"
     * for example, "photo(1).jpeg"
     * @param newFilenameWithoutSuffix
     * @param count
     * @param newFilenameSuffix
     * @return
     */
    private String assembleNewFileName(String newFilenameWithoutSuffix, int count, String newFilenameSuffix) {
        String newFilename = new StringBuilder(newFilenameWithoutSuffix)
                .append(FileConstants.LEFT_PARENTHESES_STR)
                .append(count)
                .append(FileConstants.RIGHT_PARENTHESES_STR)
                .append(newFilenameSuffix)
                .toString();
        return newFilename;
    }

    /**
     * look up the number of duplicates
     * @param entity
     * @param newFilenameWithoutSuffix
     * @return
     */
    private int getDuplicateFilename(driveHarborUserFile entity, String newFilenameWithoutSuffix) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", entity.getParentId());
        queryWrapper.eq("folder_flag", entity.getFolderFlag());
        queryWrapper.eq("user_id", entity.getUserId());
        queryWrapper.eq("del_flag", DelFlagEnum.NO.getCode());
        queryWrapper.likeRight("filename", newFilenameWithoutSuffix);
        return count(queryWrapper);
    }
}




