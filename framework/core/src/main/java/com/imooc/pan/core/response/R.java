package com.imooc.pan.core.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import java.io.Serializable;
import java.util.Objects;

/**
 * public return values
 */
// Guaranteed to return non-null
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class R<T> implements Serializable {
    /**
     * state code
     */
    private Integer code;
    /**
     * state description
     */
    private String message;
    /**
     * return date
     */
    private T data;

    private R(Integer code){
        this.code = code;

    }

    private R(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    private R(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isSuccess(){
        return Objects.equals(this.code, ResponseCode.SUCCESS.getCode());
    }

    public static <T> R<T> success(){
        return new R<T>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> R<T> success(String message){
        return new R<T>(ResponseCode.SUCCESS.getCode(), message);
    }
    public static <T> R<T> data(T data){
        return new R<T>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDesc(), data);
    }
    public static <T> R<T> fail(){
        return new R<T>(ResponseCode.ERROR.getCode());
    }
    public static <T> R<T> fail(String message){
        return new R<T>(ResponseCode.ERROR.getCode(), message);
    }
    public static <T> R<T> fail(Integer code, String message){
        return new R<T>(code, message);
    }
    public static <T> R<T> fail(ResponseCode responseCode){
        return new R<T>(responseCode.getCode(), responseCode.getDesc());
    }


}
