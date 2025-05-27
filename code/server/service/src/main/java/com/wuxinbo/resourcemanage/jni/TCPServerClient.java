package com.wuxinbo.resourcemanage.jni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * TCP client
 */
public class TCPServerClient {

//    class

    {

    }
    private static Logger logger = LoggerFactory.getLogger(TCPServerClient.class);

    /**
     * 注入tcpClient class
     * @param classLoader
     */
    public static native void  injectClassLoader(ClassLoader classLoader);
    public static Map<String,String> clientMap =new HashMap<>();


    /**
     * 发送字符串到服务端
     * @param addr
     * @param data
     */
    public static native  void sendUTFData(
                                           String addr,String data);

    /**
     * 发送字符串到客户端，如果客户端为空则不发送数据
     * @param addr 客户端地址
     * @param data 数据
     */
    public static native void sendUTFDataToClient(String addr,String data);

    /**
     * client  地址移除
     * @param address
     */
    public static void clientRemove(String address){
        logger.info("{} client remove ",address);
        clientMap.remove(address);
    }
    public static void sendDataToAllClient(String data){
        clientMap.forEach((key,value)->{

            sendUTFDataToClient(key,data);
        });
    }
    /**
     * 接受客户端的链接，并存储客户端地址
     * @param address
     */
    public static void receiveClient(String address){
        clientMap.put(address,address);
        logger.info("new client connect {}",address);
        //发送你好消息
        sendUTFDataToClient(address,"hello client");
    }
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
        logger.info("receive data {}",data);
    }
}
