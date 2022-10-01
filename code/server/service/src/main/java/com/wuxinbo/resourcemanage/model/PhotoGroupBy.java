package com.wuxinbo.resourcemanage.model;

public enum PhotoGroupBy {

    /**
     * 镜头
     */
    LENS(1),
    /**
     * 焦段
     */
    FOUCUS_LENGTH(2);
    private Integer value;

    PhotoGroupBy(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
    public static PhotoGroupBy getEnumByValue(Integer value){
        PhotoGroupBy[] values = values();
        for (PhotoGroupBy photoGroupBy : values) {
            if (photoGroupBy.value.equals(value)) {
                return photoGroupBy;
            }
        }
        return null;
    }
}
