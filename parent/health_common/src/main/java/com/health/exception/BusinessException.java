package com.health.exception;

import com.health.entity.Result;


public class BusinessException extends RuntimeException {
    private String msg;

    public BusinessException(String msg) {
        this.msg = msg;
    }

    public BusinessException() {
    }

    public String getMsg() {
        return this.msg;
    }
}
