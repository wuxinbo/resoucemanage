#ifndef IMAGEVIEWER_H
#define IMAGEVIEWER_H
#include <QMainWindow>
#include <QImage>
#include <QLabel>
#include <QScrollBar>
#include <QScrollArea>
#include <QVBoxLayout>
#include <QFileInfo>
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
private:
    void setImage(const QImage &newImage);

    Ui::Form* ui;
    QImage image;
//    QLabel *imageLabel;
    QString filePath;
    /**
     * 文件名称
     * @brief name
     */
//    QLabel *name;

};
