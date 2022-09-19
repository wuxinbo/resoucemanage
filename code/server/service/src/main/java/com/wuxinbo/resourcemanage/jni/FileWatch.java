package com.wuxinbo.resourcemanage.jni;


/**
 * 文件监控类，文件监控需要调用操作系统提供的api，在windows平台需要调用ReadDirectoryChangesW().目前已经实现windows平台
 */
public class FileWatch {


    static {
        System.loadLibrary("filewatch");
    }
    /**
     * 监控文件的变化
     * @param dir 需要监控的目录
     * @return 变更的文件名称
     */
    public native String  watchDir(String dir);
}
