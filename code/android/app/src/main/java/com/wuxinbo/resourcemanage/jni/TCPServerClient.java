package com.wuxinbo.resourcemanage.jni;


import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.wuxinbo.resourcemanage.Notification;

public class TCPServerClient {


    public static Context context;

    /**
     * 向jni 传递classloader
     * @param classLoader
     */
    public native static void injectClassLoader(ClassLoader classLoader);
    /**
     * 启动tcpserver
     * @param port
     */
    public native static void startServer(int port);

    /**
     * 连接远程服务server
     * @param addr
     */
    public native static void connect(String addr);

    /**
     * 接收数据
     * @param data
     */
    public  static void receiveData(String data){
        ((Activity)context).runOnUiThread(()->{
            Notification.ShowTextNotification(context,"收到消息",data);
            Toast.makeText(context,data,Toast.LENGTH_SHORT).show();
        });

    }

}
