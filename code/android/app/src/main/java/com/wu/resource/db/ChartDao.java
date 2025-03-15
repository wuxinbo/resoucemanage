package com.wu.resource.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wu.resource.dashboard.ChartData;

import java.util.List;


@Dao
public interface ChartDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ChartData chartData);
    @Delete
    void delete(ChartData chartData);

    @Query(" select * from ChartData where key = :key")
    ChartData query(String key);
}
