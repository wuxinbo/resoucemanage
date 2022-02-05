#ifndef GRIDIMAGELAYOUT_H
#define GRIDIMAGELAYOUT_H
#include <QList>
#include <QGridLayout>
#include "imageViewer.h"
#endif // GRIDIMAGELAYOUT_H

class GridImageLayout{


public :

    GridImageLayout(QList<QString> filePaths);
    /**
      获取初始化之后的布局文件
     * @brief getGridlayout
     * @return 九宫格布局
     */

    QGridLayout* getGridLayout();
    ~GridImageLayout();
private:
    //多张图片
    QList<ImageViewer*>* images;

    QGridLayout* gridLayout ;
};
