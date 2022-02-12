﻿#ifndef IMAGEVIEWER_H
#define IMAGEVIEWER_H
#include <QMainWindow>
#include <QImage>
#include <QLabel>
#include <QScrollBar>
#include <QScrollArea>
#include <QVBoxLayout>
#include <QFileInfo>
#include <QFutureWatcher>
#endif // IMAGEVIEWER_H

namespace Ui {
    class Form;
}

/**
 * 单个图片查看器
 * @brief The ImageViewer class
 */
class ImageViewer:public QWidget
{
    Q_OBJECT

public:

    const static int DEFAULT_HEIGHT=300;
    ImageViewer(QWidget *parent = nullptr,QFileInfo fileInfo={});
    /**
     * 获取图片展示器
    * @brief getImageLable
    * @return
    */
    QVBoxLayout* getLayout();
    bool loadFile();
    ~ ImageViewer();

private slots:
    /**
     * 图片被点击
     * @brief imageClick
     */
    void imageClick();
    /**
     * @brief 异步加载图片
     */
    void loadImage();
private:
    void setImage(const QImage &newImage);

    Ui::Form* ui;
    QImage image;
    QString filePath;

    QFuture<QImage> future;
    /**
     * @brief 异步线程监控
     */
    QFutureWatcher<QImage> watcher;
    /**
     * 文件名称
     * @brief name
     */
//    QLabel *name;

};
