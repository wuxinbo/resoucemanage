package com.wuxinbo.resourcemanage;


import com.wuxinbo.resourcemanage.jni.ImageMagick;
import com.wuxinbo.resourcemanage.jni.TCPServerClient;
import org.junit.jupiter.api.Test;

public class TCPTest {


    static {
        System.loadLibrary("xbwuc_core");
    }

    @Test
    public void sendData() throws InterruptedException {
        TCPServerClient.sendUTFData("192.168.2.3:8082","hello,worlld你还");
        TCPServerClient.sendUTFData("192.168.2.3:8082","hih你好啊");
        TCPServerClient.sendUTFData("192.168.2.3:8082","haha是不是");

    }
    @Test
    public void startServer(){
//        ImageMagick.init();
        TCPServerClient.startServer(8082);
    }

}
