package com.wuxinbo.resourcemanage;

import android.app.Application;

import androidx.room.Room;

import com.wuxinbo.resourcemanage.db.AppDataBase;


public class ResourceApplication extends Application {
        private AppDataBase db;
        @Override
        public void onCreate() {
                System.loadLibrary("core");
                super.onCreate();
                db =Room.databaseBuilder(getApplicationContext(),AppDataBase.class,"resourceDb").
                        fallbackToDestructiveMigration().
                        build();

        }

        public AppDataBase getDb() {
                return db;
        }
}
