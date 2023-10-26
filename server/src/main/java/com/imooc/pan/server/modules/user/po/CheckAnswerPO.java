package com.imooc.pan.server.modules.user.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@ApiModel(value = "User forgot the password. Verify the answer to the security question")
@Data
public class CheckAnswerPO implements Serializable {

    private static final long serialVersionUID = 6407965612511380183L;

    @ApiModelProperty(value = "Username", required = true)
    @NotBlank(message = "Username cannot be empty")
    @Pattern(regexp = "^[0-9A-Za-z]{6,16}$", message = "Username should have 6-16 digits with numbers/letters.")
    private String username;

    @ApiModelProperty(value = "Security question", required = true)
    @NotBlank(message = "Security question cannot be empty.")
    @Length(max = 100, message = "The length of the question should be no more than 100 characters.")
    private String question;

    @ApiModelProperty(value = "Security question answer", required = true)
    @NotBlank(message = "The answer cannot be empty.")
    @Length(max = 100, message = "The length of the answer should be no more than 100 characters.")
    private String answer;

}
