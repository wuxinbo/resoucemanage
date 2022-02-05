#include "imageViewer.h"
#include <QDebug>
#include <QImageReader>

ImageViewer::ImageViewer(QWidget *parent,QString filePath)
   : imageLabel(new QLabel),
     QWidget(parent)
{

//    imageLabel->setBackgroundRole(QPalette::Base);
    this->filePath =filePath;
//    imageLabel->setGeometry(0,0,40,40); //设置宽高
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
    reader.setScaledSize(QSize(300,300));
    const QImage newImage = reader.read();
    setImage(newImage);
    return true;
}


ImageViewer::~ImageViewer(){
    delete imageLabel;
}
void ImageViewer::setImage(const QImage &newImage)
{
    image = newImage;

    imageLabel->setPixmap(QPixmap::fromImage(image));

//    scaleFactor = 1.0;

//    scrollArea->setVisible(true);
//    imageLabel->adjustSize();
}


