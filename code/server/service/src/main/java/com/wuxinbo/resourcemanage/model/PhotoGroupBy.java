package com.wuxinbo.resourcemanage.model;

public enum PhotoGroupBy {

  /**
   * 镜头
   */
  LENS(1),
  /**
   * 焦段
   */
  FOUCUS_LENGTH(2),
  /**
   * 拍摄时间
   */
  SHOT_TIME(3),
  /**
   * 所有的拍摄时间
   */
  ALL_SHOT_TIME(4),

  ;
  private Integer value;

  PhotoGroupBy(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }

  public static PhotoGroupBy getEnumByValue(Integer value) {
    PhotoGroupBy[] values = values();
    for (PhotoGroupBy photoGroupBy : values) {
      if (photoGroupBy.value.equals(value)) {
        return photoGroupBy;
      }
    }
    return null;
  }
}
