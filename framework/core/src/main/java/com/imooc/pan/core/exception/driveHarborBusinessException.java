package com.imooc.pan.core.exception;

import com.imooc.pan.core.response.ResponseCode;
import lombok.Data;

/**
 * self-defined class dealing with run time exceptions
 */
@Data
public class driveHarborBusinessException extends RuntimeException{
    //exception code
    private Integer code;
    //exception message
    private String message;

    public driveHarborBusinessException(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getDesc();
    }

    public driveHarborBusinessException(Integer code, String message){
        this.code = code;
        this.message = message;
    }
    public driveHarborBusinessException(String message){
        this.code = ResponseCode.ERROR_PARAM.getCode();
        this.message = message;
    }
    public driveHarborBusinessException(){
        this.code = ResponseCode.ERROR_PARAM.getCode();
        this.message = ResponseCode.ERROR_PARAM.getDesc();
    }



}
