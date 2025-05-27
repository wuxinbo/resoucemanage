package com.wuxinbo.resourcemanage.config;

import com.wuxinbo.resourcemanage.jni.TCPServerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig implements ApplicationListener<ApplicationStartedEvent> {


    /**
     *
     */
    @Value("${tcp.server.port:8082}")
    private int tcpServerPort;
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        // 启动tcp server

        new Thread(()->{
            TCPServerClient.injectClassLoader(getClass().getClassLoader());
            TCPServerClient.startServer(tcpServerPort);
        }).start();

    }
}
