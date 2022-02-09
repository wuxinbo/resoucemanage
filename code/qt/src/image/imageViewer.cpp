#include "imageViewer.h"
#include <QDebug>
#include <QImageReader>
#include "ui_image.h"
using namespace Ui;
ImageViewer::ImageViewer(QWidget *parent,QFileInfo fileInfo)
     :QWidget(parent)
{
    ui=new Form();
    ui->setupUi(this);
    ui->imageLabel->setBackgroundRole(QPalette::Base);
    ui->fileName->setText(fileInfo.completeBaseName());
    this->filePath =fileInfo.absoluteFilePath();
//    imageLabel->setGeometry(0,0,40,40); //设置宽高
    loadFile();
    ui->imageLabel->setScaledContents(true);
    //增加点击事件
//    connect(ui->imageLabel,SIGNAL(linkActivated(filePath)),this,SLOT(imageClick()));

}

QVBoxLayout* ImageViewer::getLayout(){
    return ui->verticalLayout ;
}

void ImageViewer::imageClick(){
    qDebug("imageLCick");
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
    delete ui;
}
void ImageViewer::setImage(const QImage &newImage)
{
    image = newImage;

    ui->imageLabel->setPixmap(QPixmap::fromImage(image));

//    scaleFactor = 1.0;

//    scrollArea->setVisible(true);
//    imageLabel->adjustSize();
}


