package com.wuxinbo.common.http;

public class Result<T> {


    private boolean success =true;
    private String code;
    private String msg;
    private T res;

    public static Result success(){
        Result result =new Result();
//        result
        return result;
    }
    public static Result err(String msg){
        Result reuslt = success();
        reuslt.setMsg(msg);
        reuslt.setSuccess(false);
        return reuslt;
    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getRes() {
        return res;
    }

    public void setRes(T res) {
        this.res = res;
    }

}
