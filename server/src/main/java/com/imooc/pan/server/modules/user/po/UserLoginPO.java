package com.imooc.pan.server.modules.user.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * registered user params
 */
@Data
@ApiModel(value = "user login params")
public class UserLoginPO implements Serializable {

    private static final long serialVersionUID = -2120031555989394209L;

    @ApiModelProperty(value = "Username", required = true)
    @NotBlank(message = "Username cannot be empty.")
    @Pattern(regexp = "^[0-9A-Za-z]{6,16}$", message = "Username should have 6-16 digits with numbers/letters.")
    private String username;

    @ApiModelProperty(value = "Password", required = true)
    @NotBlank(message = "Password cannot be empty.")
    @Length(min = 8, max = 16, message = "Password should have 8-16 digits.")
    private String password;

}
