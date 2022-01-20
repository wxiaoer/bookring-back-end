package com.gyb.bookring.entity;

public class Result {
    boolean status;
    String reason;
    Object returnObj;

    public Result(boolean status, String reason, Object returnObj){
        this.status = status;
        this.reason = reason;
        this.returnObj = returnObj;
    }

    public Result(){
        this.status = true;
    }

    public Result(Object returnObj){
        this.status = true;
        this.returnObj = returnObj;
    }

    public Result(String reason){
        this.status = false;
        this.reason = reason;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Object getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(Object returnObj) {
        this.returnObj = returnObj;
    }
}

