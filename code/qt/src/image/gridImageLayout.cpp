#pragma once
#include "GridImageLayout.h"

/**
 * 网格图片布局
 * @brief GridImageLayout::GridImageLayout
 */
GridImageLayout::GridImageLayout(QWidget *parent,QList<QFileInfo> fileInfos):
    images(new QList<ImageViewer*>()),
    gridLayout(new QGridLayout())
//    QWidget(parent)
           {
    int i=0;
    for(QFileInfo file:fileInfos){
       ImageViewer *image = new ImageViewer(parent,file);
       if(file.absoluteFilePath().contains(".jpg")){ //目前只支持jpg图片展示
           images->push_back(image);
           //计算行数
           gridLayout->addWidget(image,i/DEFAULT_COLUMN,i%DEFAULT_COLUMN);
           i++;
       }

    }

}

GridImageLayout::~GridImageLayout(){
    for(ImageViewer * image: *images){
        images->pop_back();
        delete image;
    }
    delete gridLayout;
}

QGridLayout* GridImageLayout:: getGridLayout(){
    return gridLayout;
}

