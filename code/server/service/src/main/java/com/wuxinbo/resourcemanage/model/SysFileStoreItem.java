package com.wuxinbo.resourcemanage.model;

import javax.persistence.*;

/**
 * 文件基本信息
 */
@Entity
public class SysFileStoreItem extends BaseInfo{

    private String fileName;
    private String fileType;
    private Integer fileSize;
    /**
     * 节点Id
     */
    private Integer nodeId;
    /**
     * 相对路径
     */
    private String relativeUrl;
    @OneToOne
    @JoinColumn(name="nodeId",referencedColumnName = "mid",insertable = false,updatable = false)
    private SysFileStoreNode sysFileStoreNode;


    public SysFileStoreNode getSysFileStoreNode() {
        return sysFileStoreNode;
    }

    public void setSysFileStoreNode(SysFileStoreNode sysFileStoreNode) {
        this.sysFileStoreNode = sysFileStoreNode;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public String getRelativeUrl() {
        return relativeUrl;
    }

    public void setRelativeUrl(String relativeUrl) {
        this.relativeUrl = relativeUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

}
