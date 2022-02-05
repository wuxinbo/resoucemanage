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
       ImageViewer *image = new ImageViewer(parent,file.absoluteFilePath());
       if(file.absoluteFilePath().contains(".jpg")){
           images->push_back(image);
           //计算行数
           gridLayout->addWidget(image->getImageLable(),i/DEFAULT_COLUMN,i%DEFAULT_COLUMN);
           gridLayout->addWidget(new QLabel(file.completeBaseName()),i/DEFAULT_COLUMN,i%DEFAULT_COLUMN);
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

