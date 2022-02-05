#pragma once
#include "GridImageLayout.h"

/**
 * 网格图片布局
 * @brief GridImageLayout::GridImageLayout
 */
GridImageLayout::GridImageLayout(QList<QString> filePaths):
    images(new QList<ImageViewer*>()),
    gridLayout(new QGridLayout()
           ){
    int i=0;
    for(QString filePath:filePaths){
       ImageViewer *image = new ImageViewer(nullptr,filePath);
        images->push_back(image);
        gridLayout->addWidget(image->getImageLable(),0,i);
        i++;
    }
    qDebug("size is "+images->size());

}

GridImageLayout::~GridImageLayout(){
    for(ImageViewer * image: *images){
        delete image;
    }
    delete gridLayout;
}

QGridLayout* GridImageLayout:: getGridLayout(){
    return gridLayout;
}

