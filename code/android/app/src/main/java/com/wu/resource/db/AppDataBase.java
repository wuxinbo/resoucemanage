package com.wu.resource.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.wu.resource.image.PhotoDao;
import com.wu.resource.image.PhotoInfo;

@Database(entities = {PhotoInfo.class}, version = 3, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract PhotoDao photoDao();
}
