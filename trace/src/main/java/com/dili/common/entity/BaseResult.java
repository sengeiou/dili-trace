package com.dili.common.entity;

/**
 * 接口返回基础类
 * @param <T>
 */
public class BaseResult<T> {

    private int code;
    private String msg;
    private String result;
    private T data;

    public BaseResult(){}
    public BaseResult(int code, String msg){
        this.code=code;
        this.msg=msg;
        this.result=msg;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
        this.result=msg;
    }

    public T getData() {
        return data;
    }

    public BaseResult setData(T data) {
        this.data = data;
        return this;
    }

    public boolean isSuccess(){
        return ExecutionConstants.SUCCESS==this.code;
    }

    public static <T> BaseResult<T> success(){
        return new BaseResult<T>(ExecutionConstants.SUCCESS,"操作成功");
    }

    public static <T> BaseResult<T> success(T t){
        BaseResult<T> baseResult=new BaseResult<>(ExecutionConstants.SUCCESS,"操作成功");
        baseResult.setData(t);
        return baseResult;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public static <T> BaseResult<T> failure(){
        return new BaseResult<T>(ExecutionConstants.FAILURE,"操作失败");
    }

    public static <T> BaseResult<T> failure(String msg){
        return new BaseResult<T>(ExecutionConstants.FAILURE,msg);
    }


    public static <T> BaseResult<T> noLogin(){
        return new BaseResult<T>(ExecutionConstants.NO_LOGIN,"未登陆");
    }

    public static <T> BaseResult<T> noAuthority(){
        return new BaseResult<T>(ExecutionConstants.NO_AUTHORITY,"未授权");
    }
}
