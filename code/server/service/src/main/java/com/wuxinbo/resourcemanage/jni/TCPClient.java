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

    public static void receiveData(String data){
        System.out.printf("receive data  "+data);
    }
}
