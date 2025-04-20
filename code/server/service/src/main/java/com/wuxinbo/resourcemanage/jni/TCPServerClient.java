package com.wuxinbo.resourcemanage.jni;

import java.nio.ByteBuffer;

/**
 * TCP client
 */
public class TCPServerClient {


    /**
     * 发送字符串
     * @param addr
     * @param data
     */
    public static native  void sendUTFData(String addr,String data);

    /**
     *  启动tcp server
     * @param port
     */
    public static native void startServer(int port);

    /**
     * 创建directBuffer
     * @param allocate
     */
    public static native void createBuffer(ByteBuffer allocate);

    public static void receiveData(String data){
//        ByteBuffer allocate = ByteBuffer.allocate(4096);

        System.out.println("receive data  "+data);
    }
}
