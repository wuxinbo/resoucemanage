package com.wuxinbo.resourcemanage;

import android.app.Application;
import android.content.Intent;

import androidx.room.Room;

import com.wuxinbo.resourcemanage.db.AppDataBase;
import com.wuxinbo.resourcemanage.service.ManageService;


public class ResourceApplication extends Application {
        private AppDataBase db;
        @Override
        public void onCreate() {
                System.loadLibrary("xbwuc_core");
                super.onCreate();
                db =Room.databaseBuilder(getApplicationContext(),AppDataBase.class,"resourceDb").
                        fallbackToDestructiveMigration().
                        build();

        }

        public AppDataBase getDb() {
                return db;
        }
}
