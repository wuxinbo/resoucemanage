#include "imageViewer.h"
#include <QDebug>
#include <QImageReader>
#include "ui_image.h"
#include <QtConcurrent/QtConcurrent>

using namespace Ui;
ImageViewer::ImageViewer(QWidget *parent,QFileInfo fileInfo)
     :QWidget(parent)
{
    ui=new Form();
    ui->setupUi(this);
    ui->imageLabel->setBackgroundRole(QPalette::Base);
    ui->fileName->setText(fileInfo.completeBaseName());
    this->filePath =fileInfo.absoluteFilePath();
    this->setMinimumWidth(500); //设置宽高
    this->setMinimumHeight(400); //设置最小高
    ui->imageLabel->setScaledContents(true);
    loadFile();

    //增加点击事件

}

QVBoxLayout* ImageViewer::getLayout(){
    return ui->verticalLayout ;
}

void ImageViewer::imageClick(){
    qDebug("imageLCick");
}

bool ImageViewer::loadFile()
{
    QFuture<QImage> future =QtConcurrent::run([&]{
        QImageReader reader(filePath);
//        qDebug("loadFilePath:"+filePath.toUtf8());
        reader.setAutoTransform(true);
        reader.setScaledSize(QSize(300,DEFAULT_HEIGHT));
        QImage newImage = reader.read();

        return newImage;
    });
    watcher.setFuture(future);
    connect(&watcher,&QFutureWatcher<QImage>::finished,this,&ImageViewer::loadImage);
    return true;
}

void ImageViewer::loadImage(){
    setImage(watcher.future().result());
}

ImageViewer::~ImageViewer(){
    delete ui;
}
void ImageViewer::setImage(const QImage &newImage)
{
    image = newImage;
    ui->imageLabel->setPixmap(QPixmap::fromImage(image));
}


