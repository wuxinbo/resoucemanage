package com.wuxinbo.resourcemanage.image;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PhotoInfo> photos);

    @Delete
    void delete(PhotoInfo user);

    @Query("SELECT * FROM PhotoInfo order by mid desc")
    List<PhotoInfo> getAll();
}
