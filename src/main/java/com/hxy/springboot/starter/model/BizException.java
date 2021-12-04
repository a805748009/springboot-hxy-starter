package com.hxy.springboot.starter.model;

public class BizException extends RuntimeException {
    private BizHttpCode bizHttpCode;

    public BizHttpCode getBizHttpCode() {
        return bizHttpCode;
    }

    public BizException(BizHttpCode bizHttpCode) {
        this.bizHttpCode = bizHttpCode;
    }

    public BizException(String message, BizHttpCode bizHttpCode) {
        super(message);
        this.bizHttpCode = bizHttpCode;
    }

    @Override
    public String getMessage() {
        String msg = String.format("{\"code\":%s,\"msg\":%s}", bizHttpCode.code, bizHttpCode.msg);
        if (super.getMessage() != null && !super.getMessage().isEmpty()) {
            msg += ",message: " + super.getMessage();
        }
        if (this.getCause() != null) {
            msg += ",cause message:" + this.getCause().getMessage();
        }
        return msg;
    }

    @Override
    public String toString() {
        return "BizException{" +getMessage() +
                '}';
    }
}
