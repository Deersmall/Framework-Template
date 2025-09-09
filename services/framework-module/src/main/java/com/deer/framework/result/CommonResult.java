package com.deer.framework.result;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.deer.framework.Enum.ResultCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommonResult<T> implements Serializable {
    private Boolean succeed;
    private Integer code;
    private String message;
    private T data;


    public CommonResult() {
    }

    public CommonResult(boolean succeed) {
        this.succeed = succeed;
        this.code = succeed ? ResultCode.SUCCESS.getCode() : ResultCode.COMMON_FAIL.getCode();
        this.message = succeed ? ResultCode.SUCCESS.getMessage() : ResultCode.COMMON_FAIL.getMessage();
    }

    public CommonResult(boolean succeed, ResultCode resultEnum) {
        this.succeed = succeed;
        this.code = succeed ? ResultCode.SUCCESS.getCode() : (resultEnum == null ? ResultCode.COMMON_FAIL.getCode() : resultEnum.getCode());
        this.message = succeed ? ResultCode.SUCCESS.getMessage() : (resultEnum == null ? ResultCode.COMMON_FAIL.getMessage() : resultEnum.getMessage());
    }

    public CommonResult(boolean succeed, T data) {
        this.succeed = succeed;
        this.code = succeed ? ResultCode.SUCCESS.getCode() : ResultCode.COMMON_FAIL.getCode();
        this.message = succeed ? ResultCode.SUCCESS.getMessage() : ResultCode.COMMON_FAIL.getMessage();
        this.data = data;
    }

    public CommonResult(boolean succeed, ResultCode resultEnum, T data) {
        this.succeed = succeed;
        this.code = succeed ? ResultCode.SUCCESS.getCode() : (resultEnum == null ? ResultCode.COMMON_FAIL.getCode() : resultEnum.getCode());
        this.message = succeed ? ResultCode.SUCCESS.getMessage() : (resultEnum == null ? ResultCode.COMMON_FAIL.getMessage() : resultEnum.getMessage());
        this.data = data;
    }

    public static  CommonResult ok(Object data) {
        return ObjectUtils.isNotEmpty(data) ? new CommonResult(true, data) : new CommonResult(true);
    }

    public static  CommonResult no(Object data) {
        return ObjectUtils.isNotEmpty(data) ? new CommonResult(false, data) : new CommonResult(false);

    }

    public static  CommonResult toObjResult(Object data) {
        return ObjectUtils.isNotEmpty(data) ? new CommonResult(true, data) : new CommonResult(false);
    }

    public static  CommonResult toBooleanResult(boolean isOk) {
        return new CommonResult(isOk);
    }

    public static  CommonResult toBooleanResult(boolean isOk , Object data) {
        return new CommonResult(isOk , data);
    }

    public static  CommonResult toIntResult(Integer rows) {
        return rows != null && rows > 0 ? new CommonResult(true, rows) : new CommonResult(false);
    }

    @Override
    public String toString() {
        return "CommonResult{" +
                "succeed=" + succeed +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

