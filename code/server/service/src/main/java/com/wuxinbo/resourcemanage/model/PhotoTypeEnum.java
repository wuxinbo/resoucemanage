package com.wuxinbo.resourcemanage.model;

public enum PhotoTypeEnum {

    /**
     * 新增图片
     */
    NEW(1);


    private Integer value;

    PhotoTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
