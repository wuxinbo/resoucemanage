package com.wu.resource.image;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.wu.resource.db.MyTypeConverter;

import java.util.Date;

@Entity
@TypeConverters(MyTypeConverter.class)
public class PhotoInfo {


    private Integer iso;
    private String speed;
    private Integer height;
    private Integer width;
    private String aperture;
    /**
     * 文件Id
     */
    private Integer fileId;
    /**
     * 拍摄时间
     */
    private Date shotTime;
    /**
     * 焦距
     */
    private String focusLength;

    private String model;
    /**
     * 文件名称
     */
    private String fileName;
    private SysFileStoreItem sysFileStoreItem;

    public SysFileStoreItem getSysFileStoreItem() {
        return sysFileStoreItem;
    }

    public void setSysFileStoreItem(SysFileStoreItem sysFileStoreItem) {
        this.sysFileStoreItem = sysFileStoreItem;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static class SysFileStoreItem {
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

    public Date getShotTime() {
        return shotTime;
    }

    public void setShotTime(Date shotTime) {
        this.shotTime = shotTime;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }


    public PhotoInfo() {

    }


    private String lens;
    @PrimaryKey
    private Integer mid;

    public String getFocusLength() {
        return focusLength;
    }

    public void setFocusLength(String focusLength) {
        this.focusLength = focusLength;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLens() {
        return lens;
    }

    public void setLens(String lens) {
        this.lens = lens;
    }

    public String getAperture() {
        return aperture;
    }

    public void setAperture(String aperture) {
        this.aperture = aperture;
    }

  public Integer getIso() {
    return iso;
  }

  public void setIso(Integer iso) {
    this.iso = iso;
  }

  public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

}
