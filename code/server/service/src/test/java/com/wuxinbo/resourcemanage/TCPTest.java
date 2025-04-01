package com.wuxinbo.resourcemanage;


import com.wuxinbo.resourcemanage.jni.TCPClient;
import org.junit.jupiter.api.Test;

public class TCPTest {


    static {
        System.loadLibrary("core");
    }
    @Test
    public void sendData(){
        TCPClient.sendUTFData("192.168.2.5:8080","hello,worlld你好啊啊是的");
    }
}
