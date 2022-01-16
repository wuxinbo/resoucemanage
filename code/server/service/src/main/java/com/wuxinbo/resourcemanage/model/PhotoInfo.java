package com.wuxinbo.resourcemanage.model;

import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder;

/**
 * 照片信息
 */
@Entity
public class PhotoInfo {

    private Integer ISO;
    private String speed;
    private Integer height;
    private Integer width;
}
