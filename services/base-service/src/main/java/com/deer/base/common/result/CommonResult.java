package com.deer.base.common.result;

import com.deer.base.common.Enum.ResultCode;
import lombok.Data;
import org.springframework.util.ObjectUtils;
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
        if (ObjectUtils.isEmpty(data)){
            return new CommonResult(true);
        }else {
            return new CommonResult(true, data);
        }
    }

    public static  CommonResult no(Object data) {
        if (ObjectUtils.isEmpty(data)) {
            return new CommonResult(false);
        }else {
            return new CommonResult(false, data);
        }
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

