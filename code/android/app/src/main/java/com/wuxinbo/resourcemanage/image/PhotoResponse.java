package com.wuxinbo.resourcemanage.image;

import androidx.lifecycle.ViewModel;

import java.io.Serializable;
import java.util.List;

public class PhotoResponse extends ViewModel implements Serializable {

    private List<PhotoInfo> content;

    public List<PhotoInfo> getContent() {
        return content;
    }

    public void setContent(List<PhotoInfo> content) {
        this.content = content;
    }
}
