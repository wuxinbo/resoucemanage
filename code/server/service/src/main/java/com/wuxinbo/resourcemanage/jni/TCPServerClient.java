package com.wuxinbo.resourcemanage.jni;

/**
 * TCP client
 */
public class TCPClient {


    /**
     * 发送字符串
     * @param addr
     * @param data
     */
    public static native  void sendUTFData(String addr,String data);

    /**
     *  启动
     * @param port
     */
    public static native void startServer(int port);
    public static void receiveData(String data){
        System.out.printf("receive data  "+data);
    }
}
