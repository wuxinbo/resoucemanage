package com.wu.common.download;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

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
                    if (file.exists()){
                        file.delete();
                        if(!file.createNewFile()){
                            Log.w("DownloadTask","createFile is file");
                        }
                    }
                    Log.i("downloadFilePath is ",file.getPath());
                    try (FileOutputStream fileOutputStream =new FileOutputStream(file.getPath())){
                        FileUtils.copy(inputStream,fileOutputStream);
                    }
                    //通知相册更新
                    Intent scanFileIntent =new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    scanFileIntent.setData(Uri.fromFile(file));
                    context.sendBroadcast(scanFileIntent);
                } catch (FileNotFoundException e) {
                    Log.e("DownloadTask","open file fail",e);
                } catch (IOException e) {
                    Log.e("DownloadTask","write file fail",e);
                }
                finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {

                    }
                }
//                NotificationCompat.
            });
        });
    }

    public static void updateMedia(Context context,File file){
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues imageValue =new ContentValues();
        imageValue.put(MediaStore.Images.Media.DISPLAY_NAME,file.getName());
        Uri imageCollection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageCollection = MediaStore.Images.Media
                    .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        imageCollection=FileProvider.getUriForFile(context,"com.wu.resource.file_provider",file);
        Uri image = contentResolver.insert(imageCollection, imageValue);
        Log.i("Media", "updateMedia: "+image.getPath());
    }

}
