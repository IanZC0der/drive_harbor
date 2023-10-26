package com.imooc.pan.server.modules.user.context;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class CheckAnswerContext implements Serializable {

    private static final long serialVersionUID = -947015711857341702L;

    /**
     * username
     */
    private String username;

    /**
     * security question
     */
    private String question;

    /**
     * answer to the security question
     */
    private String answer;

}
