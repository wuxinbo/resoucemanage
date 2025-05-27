package com.wuxinbo.resourcemanage.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.wuxinbo.resourcemanage.jni.TCPServerClient;

public class ManageService extends Service {
    public ManageService() {
    }


    public class LocalBinder extends Binder {
        public ManageService getService() {
            return ManageService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 启动tcpServer
        new Thread(()->{
            TCPServerClient.connect("192.168.2.3:8082");

        }).start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new LocalBinder();
    }
}