package com.imooc.pan.server.modules.file.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.pan.core.exception.driveHarborBusinessException;
import com.imooc.pan.core.utils.IdUtil;
import com.imooc.pan.server.common.config.HarborServerConfig;
import com.imooc.pan.server.modules.file.context.FileChunkSaveContext;
import com.imooc.pan.server.modules.file.converter.FileConverter;
import com.imooc.pan.server.modules.file.entity.driveHarborFileChunk;
import com.imooc.pan.server.modules.file.enums.MergeFlagEnum;
import com.imooc.pan.server.modules.file.service.IFileChunkService;
import com.imooc.pan.server.modules.file.mapper.driveHarborFileChunkMapper;
import com.imooc.pan.storage.engine.core.StorageEngine;
import com.imooc.pan.storage.engine.core.context.StoreFileChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
* @author benchi
* @description 针对表【r_pan_file_chunk(file chunks information table)】的数据库操作Service实现
* @createDate 2023-10-23 21:04:43
*/
@Service
public class FileChunkServiceImpl extends ServiceImpl<driveHarborFileChunkMapper, driveHarborFileChunk>
    implements IFileChunkService {

    @Autowired
    private HarborServerConfig config;

    @Autowired
    private FileConverter fileConverter;

    @Autowired
    private StorageEngine storageEngine;

    /**
     * 1. save the chunks and record
     * 2. check if all the chunks are uploaded
     * @param context
     */
    @Override
    public synchronized void saveChunkFile(FileChunkSaveContext context) {
        doSaveChunkFile(context);
        doJudgeMergeFile(context);

    }

    /**
     * store is completed by the engine
     * save the record thereafter
     * @param context
     */
    private void doSaveChunkFile(FileChunkSaveContext context) {
        doStoreFileChunk(context);
        doSaveRecord(context);
    }

    private void doSaveRecord(FileChunkSaveContext context) {
        driveHarborFileChunk record = new driveHarborFileChunk();
        record.setId(IdUtil.get());
        record.setIdentifier(context.getIdentifier());
        record.setRealPath(context.getRealPath());
        record.setChunkNumber(context.getChunkNumber());
        record.setExpirationTime(DateUtil.offsetDay(new Date(), config.getChunkFileExpirationDays()));
        record.setCreateUser(context.getUserId());
        record.setCreateTime(new Date());
        if (!save(record)) {
            throw new driveHarborBusinessException("Chunk uploading failure.");
        }
    }

    /**
     *store is completed by the engine
     *
     * @param context
     */
    private void doStoreFileChunk(FileChunkSaveContext context) {
        try {
            StoreFileChunkContext storeFileChunkContext = fileConverter.fileChunkSaveContext2StoreFileChunkContext(context);
            storeFileChunkContext.setInputStream(context.getFile().getInputStream());
            storageEngine.storeChunk(storeFileChunkContext);
            context.setRealPath(storeFileChunkContext.getRealPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new driveHarborBusinessException("Chunk upload failure");
        }
    }


    private void doJudgeMergeFile(FileChunkSaveContext context) {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.eq("identifier", context.getIdentifier());
        queryWrapper.eq("create_user", context.getUserId());
        int count = count(queryWrapper);
        if (count == context.getTotalChunks().intValue()) {
            context.setMergeFlagEnum(MergeFlagEnum.READY);
        }
    }
}




