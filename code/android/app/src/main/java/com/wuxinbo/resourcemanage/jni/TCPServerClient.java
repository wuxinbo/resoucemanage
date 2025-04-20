package com.wuxinbo.resourcemanage.jni;


import android.content.Context;
import android.widget.Toast;

public class TCPServerClient {


    public static Context context;
    /**
     * 启动tcpserver
     * @param port
     */
    public native static void startServer(int port);


    /**
     * 接收数据
     * @param data
     */
    public  static void receiveData(String data){
        Toast.makeText(context,data,Toast.LENGTH_SHORT).show();
    }

}
