package com.hxy.springboot.starter.model;

public class TimeEntity<T> {
    private T t;
    private long timeOut;

    public TimeEntity(T t, long timeOut) {
        this.t = t;
        this.timeOut = timeOut;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }
}
