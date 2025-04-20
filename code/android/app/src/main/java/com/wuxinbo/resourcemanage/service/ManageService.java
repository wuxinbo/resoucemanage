package com.wuxinbo.resourcemanage.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.wuxinbo.resourcemanage.jni.TCPServerClient;

public class ManageService extends Service {
    public ManageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
      // 启动tcpServer
        new Thread(()->{
            TCPServerClient.startServer(8081);
        }).start();

        return null;
    }
}