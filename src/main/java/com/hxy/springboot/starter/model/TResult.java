package com.hxy.springboot.starter.model;


public class TResult<T> {
    private int code;
    private String message;
    private T data;

    public static TResult OK = new TResult<>(200,"success");

    public TResult() {
    }

    public TResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public TResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> TResult<T> success(T t){
        return new TResult<>(200,"success",t);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
