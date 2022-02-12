#pragma

#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <iostream>
#include "image/GridImageLayout.h"
#include <QDir>
#include <QStyle>
#include <QLabel>
#include <QAction>
#include <QIcon>
#include <QPixmap>
MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    // 设置窗口最大化
    showMaximized();
//    setWindowFlag(Qt::WindowType::FramelessWindowHint);
    initInput();
    //设置关闭按钮
    ui->close->setIcon(style()->standardIcon(QStyle::StandardPixmap::SP_TitleBarCloseButton));
    ui->min->setIcon(style()->standardIcon(QStyle::StandardPixmap::SP_TitleBarMinButton));
    //获取图片网格
    imageLayout =ui->imageLayout;
    QDir dir("D:\\wallerpage");
    QFileInfoList files=dir.entryInfoList();
    for(QFileInfo file:files){
        qDebug("fileName is:"+file.absoluteFilePath().toUtf8());
    }
    imageLayout->addWidget(new QLabel(QString::fromLocal8Bit("2022年1月")));
    GridImageLayout *gridLayout =new GridImageLayout(parent,files);
    imageLayout->addLayout(gridLayout->getGridLayout());
    ui->scrollAreaWidgetContents->setLayout(imageLayout);
}

void MainWindow::initInput(){
    QAction *searchAction =new QAction(QIcon(QPixmap(":/images/search.png")),"");
    ui->input->addAction(searchAction);

}
MainWindow::~MainWindow()
{
    delete ui;
    delete imageLayout;
}
/**
 * @brief 按钮点击事件监听
 */
void MainWindow::buttonClick(){
    QPlainTextEdit *input = ui->input;
    input->setPlainText("hello,word");
    qDebug("hello,world\n");
}

