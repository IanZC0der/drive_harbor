package com.imooc.pan.server.modules.user.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "user updates the password")
public class ChangePasswordPO implements Serializable {

    private static final long serialVersionUID = 6751540467000898782L;
    @ApiModelProperty(value = "Old Password", required = true)
    @NotBlank(message = "Old password cannot be empty.")
    @Length(min = 8, max = 16, message = "Password should have 8-16 digits.")
    private String oldPassword;

    @ApiModelProperty(value = "New Password", required = true)
    @NotBlank(message = "New password cannot be empty.")
    @Length(min = 8, max = 16, message = "Password should have 8-16 digits.")
    private String newPassword;
}
