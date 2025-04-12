package com.wuxinbo.resourcemanage;


import com.wuxinbo.resourcemanage.jni.TCPServerClient;
import org.junit.jupiter.api.Test;

public class TCPTest {


    static {
        System.loadLibrary("core");
    }

    @Test
    public void sendData() throws InterruptedException {
        TCPServerClient.sendUTFData("192.168.2.5:8080","hello,worlld你好啊是的");
        TCPServerClient.sendUTFData("192.168.2.5:8080","你阿訇你好");
    }
    @Test
    public void startServer(){
        TCPServerClient.startServer(8080);
    }

}
