package com.wuxinbo.resourcemanage.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 柱状图数据
 */
public class BarChartData {

    private List<String> category =new ArrayList<>();
    private List data =new ArrayList();

    public void addCategory(String data){
        category.add(data);
    }
    public void addData(Object data){
        this.data.add(data);
    }
    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
