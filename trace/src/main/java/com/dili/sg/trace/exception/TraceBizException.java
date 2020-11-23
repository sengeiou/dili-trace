package com.dili.trace.exception;

public class TraceBizException extends RuntimeException{

    public TraceBizException(){}
    public TraceBizException(String msg){
        super(msg);
    }
    public TraceBizException(String msg, Throwable t){
        super(msg,t);
    }
}
