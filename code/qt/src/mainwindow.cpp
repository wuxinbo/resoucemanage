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
#include <QScreen>
#include <memory>
MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    // 设置窗口最大化
    //showMaximized();
    //setWindowFlag(Qt::WindowType::FramelessWindowHint);
    initInput();
    //获取图片网格
    imageLayout =new QVBoxLayout(this);

    QDir dir("D:\\wallerpage");
    //获取目录内容
    QFileInfoList files=dir.entryInfoList();
    for(QFileInfo file:files){
        qDebug("fileName is:"+file.absoluteFilePath().toUtf8());
    }
    std::shared_ptr<QLabel> title(new QLabel(QStringLiteral("2022年1月")));
    imageLayout->addWidget(title.get());
    GridImageLayout *gridLayout =new GridImageLayout(parent,files);
    imageLayout->addLayout(gridLayout->getGridLayout());
    ui->scrollAreaWidgetContents->setLayout(imageLayout);

}

void MainWindow::initInput(){
    QAction *searchAction =new QAction(QIcon(QPixmap(":/images/search.png")),"");
    ui->input->addAction(searchAction);

}
void MainWindow::resizeEvent(QResizeEvent *event){
    QSize newSize =event->size();
    QString size ="resize "+QString::number(newSize.width());
    //qDebug(size);
    ui->scrollAreaWidgetContents->resize(newSize);
    //ui->scrollArea->resize(newSize);
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

