#ifndef IMAGEVIEWER_H
#define IMAGEVIEWER_H
#include <QMainWindow>
#include <QImage>
#include <QLabel>
#include <QScrollBar>
#include <QScrollArea>

#endif // IMAGEVIEWER_H

/**
 * 单个图片查看器
 * @brief The ImageViewer class
 */
class ImageViewer:public QWidget
{
    Q_OBJECT

public:
    ImageViewer(QWidget *parent = nullptr,QString filePath ="");
    /**
     * 获取图片展示器
    * @brief getImageLable
    * @return
    */
    QLabel* getImageLable();
    bool loadFile();
    ~ ImageViewer();

private slots:

private:
    void setImage(const QImage &newImage);
    QImage image;
    QLabel *imageLabel;
    QString filePath;

};
