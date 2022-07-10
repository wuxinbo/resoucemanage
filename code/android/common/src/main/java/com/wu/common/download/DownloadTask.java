package com.wu.common.download;

import androidx.core.app.NotificationCompat;

import com.wu.common.http.HttpUtil;

/**
 * 文件下载任务
 */
public class DownloadTask {

    /**
     * http 文件下载
     * @param url
     */
    public void download(String url){
        HttpUtil.executorService.execute(()->{
            HttpUtil.downloadFile(url,inputStream->{
//                NotificationCompat.
            });
        });
    }
}
