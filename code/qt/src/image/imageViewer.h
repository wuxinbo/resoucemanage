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
class ImageViewer
{


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
    void open();
    void saveAs();
    void zoomIn();
    void zoomOut();
    void normalSize();
    void fitToWindow();
    void about();

private:
    void setImage(const QImage &newImage);
    void adjustScrollBar(QScrollBar *scrollBar, double factor);

    QImage image;
    QLabel *imageLabel;
    QScrollArea *scrollArea;
    double scaleFactor = 1;
    QString filePath ;
    QAction *saveAsAct;
    QAction *printAct;
    QAction *copyAct;
    QAction *zoomInAct;
    QAction *zoomOutAct;
    QAction *normalSizeAct;
    QAction *fitToWindowAct;
};
