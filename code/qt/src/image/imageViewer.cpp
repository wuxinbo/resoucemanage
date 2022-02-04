#include "imageViewer.h"
#include <QDebug>
#include <QImageReader>

ImageViewer::ImageViewer(QWidget *parent,QString filePath)
   : imageLabel(new QLabel)
   , scrollArea(new QScrollArea)
{

    imageLabel->setBackgroundRole(QPalette::Base);
    this->filePath =filePath;
    imageLabel->setFixedSize(200,200); //设置宽高
    loadFile();
    imageLabel->setScaledContents(true);

}
QLabel* ImageViewer::getImageLable(){
    return imageLabel ;
}

bool ImageViewer::loadFile()
{
    QImageReader reader(filePath);
    qDebug("loadFilePath:"+filePath.toUtf8());
    reader.setAutoTransform(true);
    const QImage newImage = reader.read();
    setImage(newImage);
    return true;
}


ImageViewer::~ImageViewer(){
    delete imageLabel;
    delete scrollArea;
}
void ImageViewer::setImage(const QImage &newImage)
{
    image = newImage;
    imageLabel->setPixmap(QPixmap::fromImage(image));

//    scaleFactor = 1.0;

//    scrollArea->setVisible(true);
    imageLabel->adjustSize();
}



//! [9]
void ImageViewer::zoomIn()
//! [9] //! [10]
{
//    scaleImage(1.25);
}

void ImageViewer::zoomOut()
{
//    scaleImage(0.8);
}

//! [10] //! [11]
void ImageViewer::normalSize()
//! [11] //! [12]
{
    imageLabel->adjustSize();
    scaleFactor = 1.0;
}
//! [12]

//! [24]

//! [25]
void ImageViewer::adjustScrollBar(QScrollBar *scrollBar, double factor)
//! [25] //! [26]
{
    scrollBar->setValue(int(factor * scrollBar->value()
                            + ((factor - 1) * scrollBar->pageStep()/2)));
}
