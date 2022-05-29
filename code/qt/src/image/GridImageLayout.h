#ifndef GRIDIMAGELAYOUT_H
#define GRIDIMAGELAYOUT_H
#include <QList>
#include <QGridLayout>
#include <QFileInfo>
#include "imageViewer.h"
class GridImageLayout: public QGridLayout{

       Q_OBJECT
public :
    //每一行的列数
    const static int DEFAULT_COLUMN =4;
    /**
     *
     * @brief 默认不按照列进行合并
     */
    const static int DEFAULT_COLUMN_SPAN =1;
    GridImageLayout(QWidget *parent = nullptr,QList<QFileInfo> fileInfos ={});
    /**

     * @brief 获取初始化之后的布局文件
     * @return 九宫格布局
     */

    QGridLayout* getGridLayout();

    ~GridImageLayout();
private:
    //多张图片
    QList<ImageViewer*>* images;

    QGridLayout* gridLayout ;
};
#endif // GRIDIMAGELAYOUT_H
