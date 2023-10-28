package com.imooc.pan.server.modules.user.converter;

import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import com.imooc.pan.server.modules.user.context.ChangePasswordContext;
import com.imooc.pan.server.modules.user.context.CheckAnswerContext;
import com.imooc.pan.server.modules.user.context.CheckUsernameContext;
import com.imooc.pan.server.modules.user.context.ResetPasswordContext;
import com.imooc.pan.server.modules.user.context.UserLoginContext;
import com.imooc.pan.server.modules.user.context.UserRegisterContext;
import com.imooc.pan.server.modules.user.entity.driveHarborUser;
import com.imooc.pan.server.modules.user.po.ChangePasswordPO;
import com.imooc.pan.server.modules.user.po.CheckAnswerPO;
import com.imooc.pan.server.modules.user.po.CheckUsernamePO;
import com.imooc.pan.server.modules.user.po.ResetPasswordPO;
import com.imooc.pan.server.modules.user.po.UserLoginPO;
import com.imooc.pan.server.modules.user.po.UserRegisterPO;
import com.imooc.pan.server.modules.user.vo.UserInfoVO;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-28T15:30:20-0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 1.8.0_341 (Oracle Corporation)"
)
@Component
public class UserConverterImpl implements UserConverter {

    @Override
    public UserRegisterContext userRegisterPO2UserRegisterContext(UserRegisterPO userRegisterPO) {
        if ( userRegisterPO == null ) {
            return null;
        }

        UserRegisterContext userRegisterContext = new UserRegisterContext();

        userRegisterContext.setUsername( userRegisterPO.getUsername() );
        userRegisterContext.setPassword( userRegisterPO.getPassword() );
        userRegisterContext.setQuestion( userRegisterPO.getQuestion() );
        userRegisterContext.setAnswer( userRegisterPO.getAnswer() );

        return userRegisterContext;
    }

    @Override
    public driveHarborUser userRegisterContext2DriveHarborUser(UserRegisterContext userRegisterContext) {
        if ( userRegisterContext == null ) {
            return null;
        }

        driveHarborUser driveHarborUser = new driveHarborUser();

        driveHarborUser.setUsername( userRegisterContext.getUsername() );
        driveHarborUser.setQuestion( userRegisterContext.getQuestion() );
        driveHarborUser.setAnswer( userRegisterContext.getAnswer() );

        return driveHarborUser;
    }

    @Override
    public UserLoginContext userLoginPO2UserLoginContext(UserLoginPO userLoginPO) {
        if ( userLoginPO == null ) {
            return null;
        }

        UserLoginContext userLoginContext = new UserLoginContext();

        userLoginContext.setUsername( userLoginPO.getUsername() );
        userLoginContext.setPassword( userLoginPO.getPassword() );

        return userLoginContext;
    }

    @Override
    public CheckUsernameContext checkUsernamePO2CheckUsernameContext(CheckUsernamePO checkUsernamePO) {
        if ( checkUsernamePO == null ) {
            return null;
        }

        CheckUsernameContext checkUsernameContext = new CheckUsernameContext();

        checkUsernameContext.setUsername( checkUsernamePO.getUsername() );

        return checkUsernameContext;
    }

    @Override
    public CheckAnswerContext checkAnswerPO2CheckAnswerContext(CheckAnswerPO checkAnswerPO) {
        if ( checkAnswerPO == null ) {
            return null;
        }

        CheckAnswerContext checkAnswerContext = new CheckAnswerContext();

        checkAnswerContext.setUsername( checkAnswerPO.getUsername() );
        checkAnswerContext.setQuestion( checkAnswerPO.getQuestion() );
        checkAnswerContext.setAnswer( checkAnswerPO.getAnswer() );

        return checkAnswerContext;
    }

    @Override
    public ResetPasswordContext resetPasswordPO2ResetPasswordContext(ResetPasswordPO resetPasswordPO) {
        if ( resetPasswordPO == null ) {
            return null;
        }

        ResetPasswordContext resetPasswordContext = new ResetPasswordContext();

        resetPasswordContext.setUsername( resetPasswordPO.getUsername() );
        resetPasswordContext.setPassword( resetPasswordPO.getPassword() );
        resetPasswordContext.setToken( resetPasswordPO.getToken() );

        return resetPasswordContext;
    }

    @Override
    public ChangePasswordContext changePasswordPO2ChangePasswordContext(ChangePasswordPO changePasswordPO) {
        if ( changePasswordPO == null ) {
            return null;
        }

        ChangePasswordContext changePasswordContext = new ChangePasswordContext();

        changePasswordContext.setOldPassword( changePasswordPO.getOldPassword() );
        changePasswordContext.setNewPassword( changePasswordPO.getNewPassword() );

        return changePasswordContext;
    }

    @Override
    public UserInfoVO assembleUserInfoVO(driveHarborUser aUser, driveHarborUserFile aFile) {
        if ( aUser == null && aFile == null ) {
            return null;
        }

        UserInfoVO userInfoVO = new UserInfoVO();

        if ( aUser != null ) {
            userInfoVO.setUsername( aUser.getUsername() );
        }
        if ( aFile != null ) {
            userInfoVO.setRootFileId( aFile.getFileId() );
            userInfoVO.setRootFilename( aFile.getFileName() );
        }

        return userInfoVO;
    }
}
