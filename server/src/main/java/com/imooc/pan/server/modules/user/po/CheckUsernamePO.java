package com.imooc.pan.server.modules.user.po;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aspectj.lang.annotation.DeclareAnnotation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@ApiModel(value = "User forgot the password. Verify the user name")
@Data
public class CheckUsernamePO implements Serializable {

    private static final long serialVersionUID = 1795641889740242870L;

    @ApiModelProperty(value = "Username", required = true)
    @NotBlank(message = "Username cannot be empty")
    @Pattern(regexp = "^[0-9A-Za-z]{6,16}$", message = "Username should have 6-16 digits with numbers/letters.")
    private String username;

}
