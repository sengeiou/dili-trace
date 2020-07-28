package com.dili.common.exception;

public class TraceBusinessException extends RuntimeException{

    public TraceBusinessException(){}
    public TraceBusinessException(String msg){
        super(msg);
    }
    public TraceBusinessException(String msg, Throwable t){
        super(msg,t);
    }
 
}
