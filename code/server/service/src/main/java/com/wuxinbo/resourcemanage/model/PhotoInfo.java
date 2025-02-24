package com.wuxinbo.resourcemanage.model;

import antlr.StringUtils;
import com.drew.metadata.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 照片信息
 */
@Entity
public class PhotoInfo extends BaseInfo{

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shotTime ;
    /**
     * 焦距
     */
    private String focusLength;
    /**
     * 收藏
     */
    @Column(name = "likes")
    private Integer like;
    /**
     * 照片评级
     */
    private BigDecimal rate;
    private String model ;

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Date getShotTime() {
        return shotTime;
    }

    public void setShotTime(Date shotTime) {
        this.shotTime = shotTime;
    }

    @Transient
    private Collection<Tag> tags ;
    public void addtags(Collection<Tag> tags){
        if (this.tags==null){
            this.tags =tags;
        }else{
            this.tags.addAll(tags);
        }

    }
    @OneToOne
    @JoinColumn(name="fileId",referencedColumnName = "mid",nullable = false,insertable = false,updatable = false)
    private SysFileStoreItem sysFileStoreItem;

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public SysFileStoreItem getSysFileStoreItem() {
        return sysFileStoreItem;
    }

    public void setSysFileStoreItem(SysFileStoreItem sysFileStoreItem) {
        this.sysFileStoreItem = sysFileStoreItem;
    }

    public PhotoInfo(){

    }


    public PhotoInfo parsetagInfo(Collection<Tag> tags){
        for (Tag tag : tags) {
            System.out.println(tag.getTagName()+":"+tag.getDescription());
            if (tag.getDescription()==null){
                continue;
            }
            if (tag.getTagName().equals("ISO Speed Ratings")){
                ISO=Integer.parseInt(tag.getDescription());
            }else if(tag.getTagName().equals("Exposure Time")){
                speed =tag.getDescription();
            }else if(tag.getTagName().equals("F-Number")){
                aperture =tag.getDescription();
            }
            else if (tag.getTagName().equals("Image Width")){
                width =Integer.parseInt(tag.getDescription().split(" ")[0]);
            }
            else if (tag.getTagName().equals("Image Height")){
                height =Integer.parseInt(tag.getDescription().split(" ")[0]);
            }
            else if (tag.getTagName().equals("Date/Time Original")){
                try {
                    shotTime =new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(tag.getDescription());
                } catch (ParseException e) {

                }
            }else if (tag.getTagName().equals("Focal Length")){
                focusLength =tag.getDescription();
            }
            else if (tag.getTagName().equals("Model")){
                model =tag.getDescription();
            }
            else if (tag.getTagName().equals("Lens Specification")){
                lens =tag.getDescription();
            }
        }
        return this;
    }
    @Column(name = "lens")
    private String lens;
    @Id
    @GeneratedValue()
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
