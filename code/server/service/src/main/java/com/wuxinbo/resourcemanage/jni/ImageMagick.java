package com.wuxinbo.resourcemanage.jni;

import com.wuxinbo.resourcemanage.model.PhotoInfo;

import java.io.File;

/**
 * imageMagick 图片处理 jni 接口
 */
public class ImageMagick {


    static {
        init();
    }

    /**
     * 系统启动时初始化
     */
    public static native void init();

    /**
     * 图片缩放 
     * @param src 原始图片地址
     * @param dest 目标图片地址
     * @param scale 缩放比例，计算公式为 新分辨率 = 原始分辨率/scale，
     * 比如原始图片分辨率为1920*1080，scale=10，则新分辨率为192*108
     * @return code 0:成功，-1:失败
     */
    public static native int resize(String src, String dest, int scale);

    /**
     * 图片压缩
     * @param photoInfo
     */
    public static void compressImage(PhotoInfo photoInfo){
        String filepath = photoInfo.getOriginFilePath();
        File thumbFile = new File(photoInfo.getThumbFilePath());
        if (!thumbFile.exists()) {
            int code = ImageMagick.resize(filepath, thumbFile.getPath(), 10);
        }

    }
    /**
     * 资源销毁
     */
    public static native void destroy();
    
}
