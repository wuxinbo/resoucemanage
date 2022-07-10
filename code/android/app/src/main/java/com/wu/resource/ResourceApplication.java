package com.wu.resource;

import android.app.Application;

import androidx.room.Room;

import com.wu.resource.db.AppDataBase;

public class ResourceApplication extends Application {
        private AppDataBase db;
        @Override
        public void onCreate() {
                super.onCreate();
                db =Room.databaseBuilder(getApplicationContext(),AppDataBase.class,"resourceDb").
                        fallbackToDestructiveMigration().
                        build();
        }

        public AppDataBase getDb() {
                return db;
        }
}
