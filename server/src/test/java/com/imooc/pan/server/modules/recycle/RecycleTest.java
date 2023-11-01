package com.imooc.pan.server.modules.recycle;

import cn.hutool.core.lang.Assert;
import com.google.common.collect.Lists;
import com.imooc.pan.core.exception.driveHarborBusinessException;
import com.imooc.pan.server.driveHarborServerLauncher;
import com.imooc.pan.server.modules.file.context.CreateFolderContext;
import com.imooc.pan.server.modules.file.context.DeleteFileContext;
import com.imooc.pan.server.modules.file.service.IUserFileService;
import com.imooc.pan.server.modules.file.vo.DriveHarborUserFileVO;
import com.imooc.pan.server.modules.recycle.context.DeleteContext;
import com.imooc.pan.server.modules.recycle.context.QueryRecycleFileListContext;
import com.imooc.pan.server.modules.recycle.context.RestoreContext;
import com.imooc.pan.server.modules.recycle.service.IRecycleService;
import com.imooc.pan.server.modules.user.context.UserRegisterContext;
import com.imooc.pan.server.modules.user.service.IUserService;
import com.imooc.pan.server.modules.user.vo.UserInfoVO;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = driveHarborServerLauncher.class)
@Transactional
public class RecycleTest {
    @Autowired
    private IUserFileService iUserFileService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IRecycleService iRecycleService;

    /**
     * test query file list in the trash can success
     * 1. create folder
     * 2. delete folder
     */
    @Test
    public void testQueryRecyclesSuccess() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        // create the folder
        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-old");

        Long fileId = iUserFileService.createFolder(context);
        Assert.notNull(fileId);

        // delete the folder
        DeleteFileContext deleteFileContext = new DeleteFileContext();
        List<Long> fileIdList = Lists.newArrayList();
        fileIdList.add(fileId);
        deleteFileContext.setFileIdList(fileIdList);
        deleteFileContext.setUserId(userId);
        iUserFileService.deleteFile(deleteFileContext);

        // query the file list, the size expected to be 1
        QueryRecycleFileListContext queryRecycleFileListContext = new QueryRecycleFileListContext();
        queryRecycleFileListContext.setUserId(userId);
        List<DriveHarborUserFileVO> recycles = iRecycleService.recycles(queryRecycleFileListContext);

        Assert.isTrue(CollectionUtils.isNotEmpty(recycles) && recycles.size() == 1);
    }

    /**
     * test restore success
     */
    @Test
    public void testFileRestoreSuccess() {

        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        // create foler
        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-old");

        Long fileId = iUserFileService.createFolder(context);
        Assert.notNull(fileId);

        // delete folder
        DeleteFileContext deleteFileContext = new DeleteFileContext();
        List<Long> fileIdList = Lists.newArrayList();
        fileIdList.add(fileId);
        deleteFileContext.setFileIdList(fileIdList);
        deleteFileContext.setUserId(userId);
        iUserFileService.deleteFile(deleteFileContext);

        // restore
        RestoreContext restoreContext = new RestoreContext();
        restoreContext.setUserId(userId);
        restoreContext.setFileIdList(Lists.newArrayList(fileId));
        iRecycleService.restore(restoreContext);
    }

    /**
     * test restore failure-wrong userID
     */
    @Test(expected = driveHarborBusinessException.class)
    public void testFileRestoreFailByWrongUserId() {

        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        // create folder
        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-old");

        Long fileId = iUserFileService.createFolder(context);
        Assert.notNull(fileId);

        // delete folder
        DeleteFileContext deleteFileContext = new DeleteFileContext();
        List<Long> fileIdList = Lists.newArrayList();
        fileIdList.add(fileId);
        deleteFileContext.setFileIdList(fileIdList);
        deleteFileContext.setUserId(userId);
        iUserFileService.deleteFile(deleteFileContext);

        // restore
        RestoreContext restoreContext = new RestoreContext();
        restoreContext.setUserId(userId + 1);
        restoreContext.setFileIdList(Lists.newArrayList(fileId));
        iRecycleService.restore(restoreContext);
    }

    /**
     * test file restore failure-repeated name in the path
     */
    @Test(expected = driveHarborBusinessException.class)
    public void testFileRestoreFailByWrongFilename1() {

        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        // create folder
        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-1");

        Long fileId = iUserFileService.createFolder(context);
        Assert.notNull(fileId);

        // delete folder
        DeleteFileContext deleteFileContext = new DeleteFileContext();
        List<Long> fileIdList = Lists.newArrayList();
        fileIdList.add(fileId);
        deleteFileContext.setFileIdList(fileIdList);
        deleteFileContext.setUserId(userId);
        iUserFileService.deleteFile(deleteFileContext);

        context.setFolderName("folder-name-1");
        fileId = iUserFileService.createFolder(context);
        Assert.notNull(fileId);

        // restore
        RestoreContext restoreContext = new RestoreContext();
        restoreContext.setUserId(userId);
        restoreContext.setFileIdList(Lists.newArrayList(fileId));
        iRecycleService.restore(restoreContext);
    }

    /**
     * test restore failure-repeated name in the files
     */
    @Test(expected = driveHarborBusinessException.class)
    public void testFileRestoreFailByWrongFilename2() {

        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        // create folder
        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-1");
        Long fileId1 = iUserFileService.createFolder(context);
        Assert.notNull(fileId1);

        // delete foler
        DeleteFileContext deleteFileContext = new DeleteFileContext();
        List<Long> fileIdList = Lists.newArrayList();
        fileIdList.add(fileId1);
        deleteFileContext.setFileIdList(fileIdList);
        deleteFileContext.setUserId(userId);
        iUserFileService.deleteFile(deleteFileContext);

        context.setFolderName("folder-name-1");
        Long fileId2 = iUserFileService.createFolder(context);
        Assert.notNull(fileId2);

        fileIdList.add(fileId2);
        iUserFileService.deleteFile(deleteFileContext);

        // restore
        RestoreContext restoreContext = new RestoreContext();
        restoreContext.setUserId(userId);
        restoreContext.setFileIdList(Lists.newArrayList(fileId1, fileId2));
        iRecycleService.restore(restoreContext);
    }

    /**
     * test trash can file delete failure-wrong ID
     */
    @Test(expected = driveHarborBusinessException.class)
    public void testFileDeleteFailByWrongUserId() {

        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        // create folder
        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-1");
        Long fileId = iUserFileService.createFolder(context);
        Assert.notNull(fileId);

        // delete folder
        DeleteFileContext deleteFileContext = new DeleteFileContext();
        List<Long> fileIdList = Lists.newArrayList();
        fileIdList.add(fileId);
        deleteFileContext.setFileIdList(fileIdList);
        deleteFileContext.setUserId(userId);
        iUserFileService.deleteFile(deleteFileContext);

        // delete permenantly
        DeleteContext deleteContext = new DeleteContext();
        deleteContext.setUserId(userId + 1);
        deleteContext.setFileIdList(Lists.newArrayList(fileId));
        iRecycleService.delete(deleteContext);
    }

    /**
     * test trash can delete success
     */
    @Test
    public void testFileDeleteSuccess() {

        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        // create folder
        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-1");
        Long fileId = iUserFileService.createFolder(context);
        Assert.notNull(fileId);

        // delete the folder
        DeleteFileContext deleteFileContext = new DeleteFileContext();
        List<Long> fileIdList = Lists.newArrayList();
        fileIdList.add(fileId);
        deleteFileContext.setFileIdList(fileIdList);
        deleteFileContext.setUserId(userId);
        iUserFileService.deleteFile(deleteFileContext);

        // delete folder
        DeleteContext deleteContext = new DeleteContext();
        deleteContext.setUserId(userId);
        deleteContext.setFileIdList(Lists.newArrayList(fileId));
        iRecycleService.delete(deleteContext);
    }



    private Long register() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = iUserService.register(context);
        Assert.isTrue(register.longValue() > 0L);
        return register;
    }

    /**
     * query the info the user logged in
     *
     * @param userId
     * @return
     */
    private UserInfoVO info(Long userId) {
        UserInfoVO userInfoVO = iUserService.info(userId);
        Assert.notNull(userInfoVO);
        return userInfoVO;
    }
    /**
     * user register context
     *
     * @return
     */
    private UserRegisterContext createUserRegisterContext() {
        UserRegisterContext context = new UserRegisterContext();
        context.setUsername(USERNAME);
        context.setPassword(PASSWORD);
        context.setQuestion(QUESTION);
        context.setAnswer(ANSWER);
        return context;
    }




    private final static String USERNAME = "ian";
    private final static String PASSWORD = "123456789";
    private final static String QUESTION = "question";
    private final static String ANSWER = "answer";
}
