package com.wuxinbo.resourcemanage.model;

public class PhotoEvent {

    private int type ;

    private String msg;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void newPhoto(){
        type =PhotoTypeEnum.NEW.getValue();
        msg= "新照片创建";
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
