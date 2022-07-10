package com.wu.common.download;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.wu.common.http.HttpUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件下载任务
 */
public class DownloadTask {

    /**
     * http 文件下载
     * @param url
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void download(String url, Context context, String fileName){
        HttpUtil.executorService.execute(()->{
            HttpUtil.downloadFile(url,inputStream->{
                File filesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                filesDir =new File(filesDir,"resource");
                if (!filesDir.exists()){
                    filesDir.mkdirs();
                }
                File file =new File(filesDir,fileName);
                try {
                    Log.i("downloadFilePath is ",file.getPath());
                    FileOutputStream fileOutputStream =new FileOutputStream(file.getPath());
                    FileUtils.copy(inputStream,fileOutputStream);
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE),file.getPath());
                } catch (FileNotFoundException e) {
                    Log.e("DownloadTask","open file fail",e);
                } catch (IOException e) {
                    Log.e("DownloadTask","write file fail",e);

                }
//                NotificationCompat.
            });
        });
    }
}
