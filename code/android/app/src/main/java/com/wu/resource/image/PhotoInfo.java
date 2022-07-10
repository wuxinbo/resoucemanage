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


    private Integer ISO;
    private String speed;
    private Integer height;
    private Integer width;
    private String  aperture;
    /**
     * 文件Id
     */
    private Integer fileId;
    /**
     * 拍摄时间
     */
    private Date shotTime ;
    /**
     * 焦距
     */
    private String focusLength;

    private String model ;
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


    public PhotoInfo(){

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

    public Integer getISO() {
        return ISO;
    }

    public void setISO(Integer ISO) {
        this.ISO = ISO;
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
