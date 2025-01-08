package com.wu.resource.dashboard;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChartData {


    @PrimaryKey
    @NonNull
    private String key;

    private String value;

    public static final String SHOT_TIME_KEY="shotTime";
    public static final String LENS_KEY="lens";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
