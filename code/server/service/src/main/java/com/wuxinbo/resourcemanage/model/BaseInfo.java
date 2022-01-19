package com.wuxinbo.resourcemanage.model;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import java.util.Date;

@Inheritance
public class BaseInfo {


    /**
     * 创建时间
     */
    @Column(name ="create_time")
    protected Date createTime;
    /**
     * 更新时间
     */
    @Column(name ="update_time")
    protected Date updateTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
