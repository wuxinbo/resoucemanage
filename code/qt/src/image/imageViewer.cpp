#include <QDebug>
#include <QImageReader>
#include "ui_image.h"
#include <QtConcurrent/QtConcurrent>
#include "imageloadevent.h"
#include "imageViewer.h"
using namespace Ui;
ImageViewer::ImageViewer(QWidget *parent,QFileInfo fileInfo)
     :QWidget(parent)
{
    ui=new Form();
    ui->setupUi(this);
    //ui->imageLabel->setBackgroundRole(QPalette::Base);
    ui->fileName->setText(fileInfo.completeBaseName());
    this->filePath =fileInfo.absoluteFilePath();
    ui->imageLabel->setScaledContents(true);
    ui->imageLabel->setStyleSheet("border-radius:20px");
    loadFile();
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
        //reader.setAutoTransform(true);
        double width =reader.size().width();
        double height =reader.size().height();
        double ratio =width/height;
        this->ratio =(int) ratio;
        int newWidth =DEFAULT_HEIGHT*ratio;
        reader.setScaledSize(QSize(newWidth,DEFAULT_HEIGHT));
        QImage newImage = reader.read();
        return newImage;
    });
    //当线程执行完成之后，才开始显示在窗口中
    connect(&watcher,&QFutureWatcher<QImage>::finished,this,&ImageViewer::loadImage);
    watcher.setFuture(future);
    return true;
}
QImage ImageViewer::getQImage(){
    return image;
}
void ImageViewer::loadImage(){
    setImage(watcher.future().result());
}

ImageViewer::~ImageViewer(){
    delete ui;
}
void ImageViewer::mousePressEvent(QMouseEvent *event){
    QString info ="lable clicked"+(ui->fileName->text());
    qDebug(info.toUtf8());

}
void ImageViewer::enterEvent(QEvent *event){
    qDebug("enter");
    ui->imageLabel->setStyleSheet("border:1px solid red;");
}
void ImageViewer::leaveEvent(QEvent *event){
    qDebug("leave");
    ui->imageLabel->setStyleSheet("");
}
void ImageViewer::setImage(const QImage &newImage)
{
    image = newImage;
    this->setMinimumWidth(newImage.size().width());
    //生成完整的名字
    QString info =ui->fileName->text()+" "+QString::number(newImage.size().width())+"X"+
            QString::number(DEFAULT_HEIGHT);
    ui->fileName->setText(info);
    ui->imageLabel->setPixmap(QPixmap::fromImage(image));

}


