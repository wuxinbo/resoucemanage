package com.wuxinbo.resourcemanage.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SysFileStoreNode extends BaseInfo{
    /**
     * 节点名称
     */
    private String fileNodeName;
    /**
     * 文件路径
     */
    private String localPath;
    @Id
    @GeneratedValue()
    private Integer mid;

    public String getFileNodeName() {
        return fileNodeName;
    }

    public void setFileNodeName(String fileNodeName) {
        this.fileNodeName = fileNodeName;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }
}
