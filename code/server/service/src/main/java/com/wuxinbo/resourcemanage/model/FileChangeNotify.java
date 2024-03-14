package com.wuxinbo.resourcemanage.model;

import static com.wuxinbo.resourcemanage.model.FileChangeNotify.ActionEnum.FILE_ACTION_ADDED;
import static com.wuxinbo.resourcemanage.model.FileChangeNotify.ActionEnum.FILE_ACTION_REMOVED;

/**
 * 文件信息变更监听
 */
public class FileChangeNotify {

  /**
   * 修改目录下的文件路径
   */
  private String filePath;
  /**
   * 操作。{@link ActionEnum}
   */
  private Integer action;

  /**
   * 文件是否删除
   *
   * @return true -是，false-不是
   */
  public boolean isRemoved() {
    return FILE_ACTION_REMOVED.value.equals(action);
  }
  public boolean isAdd(){
    return FILE_ACTION_ADDED.value.equals(action);
  }
  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public Integer getAction() {
    return action;
  }

  public void setAction(int action) {
    this.action = action;
  }

  public enum ActionEnum {
    /**
     * The file was added to the directory.
     */
    FILE_ACTION_ADDED(1),
    /**
     * The file was removed from the directory.
     */
    FILE_ACTION_REMOVED(2),
    /**
     * The file was modified. This can be a change in the time stamp or attributes.
     */
    FILE_ACTION_MODIFIED(3),
    /**
     * The file was renamed and this is the old name.
     */
    FILE_ACTION_RENAMED_OLD_NAME(4),
    /**
     * The file was renamed and this is the new name.
     */
    FILE_ACTION_RENAMED_NEW_NAME(5);

    private Integer value;

    ActionEnum(Integer value) {
      this.value = value;
    }

    public Integer getValue() {
      return value;
    }
  }
}
