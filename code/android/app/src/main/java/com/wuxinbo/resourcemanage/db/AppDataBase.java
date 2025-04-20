package com.wuxinbo.resourcemanage.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.wuxinbo.resourcemanage.dashboard.ChartData;
import com.wuxinbo.resourcemanage.image.PhotoDao;
import com.wuxinbo.resourcemanage.image.PhotoInfo;

@Database(entities = {PhotoInfo.class, ChartData.class}, version = 4, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract PhotoDao photoDao();

    public abstract ChartDao chartDao();
}
