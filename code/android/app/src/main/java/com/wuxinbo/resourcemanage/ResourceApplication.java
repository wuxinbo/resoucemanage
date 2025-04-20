package com.wuxinbo.resourcemanage;

import android.app.Application;

import androidx.room.Room;

import com.wuxinbo.resourcemanage.db.AppDataBase;
import com.wuxinbo.resourcemanage.jni.TCPServerClient;

import dalvik.system.DexClassLoader;

public class ResourceApplication extends Application {
        private AppDataBase db;
        @Override
        public void onCreate() {
                System.loadLibrary("core");
                super.onCreate();
                db =Room.databaseBuilder(getApplicationContext(),AppDataBase.class,"resourceDb").
                        fallbackToDestructiveMigration().
                        build();
            try {
                Class<?> aClass = Class.forName("com.wuxinbo.resourcemanage.jni.TCPServerClient");

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            TCPServerClient.context =this;
                new Thread(()->{
                        TCPServerClient.startServer(8081);
                }).start();
        }

        public AppDataBase getDb() {
                return db;
        }
}
