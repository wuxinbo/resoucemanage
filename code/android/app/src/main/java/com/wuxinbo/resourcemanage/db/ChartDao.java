package com.wuxinbo.resourcemanage.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wuxinbo.resourcemanage.dashboard.ChartData;


@Dao
public interface ChartDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ChartData chartData);
    @Delete
    void delete(ChartData chartData);

    @Query(" select * from ChartData where key = :key")
    ChartData query(String key);
}
