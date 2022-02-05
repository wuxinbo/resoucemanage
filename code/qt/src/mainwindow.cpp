#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <iostream>
#include "image/GridImageLayout.h"
#include <QDir>
MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    // 设置窗口最大化
    showMaximized();
    QPushButton *searchButton =ui->searchButton;
    searchButton->setText("查询");
    connect(searchButton,SIGNAL(clicked()),this,SLOT(buttonClick()));
    qDebug("heloo");
    //获取图片网格
    imageLayout =ui->imageLayout;
    QDir dir("D:\\wallerpage");
    QFileInfoList files=dir.entryInfoList();
    for(QFileInfo file:files){
        qDebug("fileName is:"+file.absoluteFilePath().toUtf8());
    }
    GridImageLayout *gridLayout =new GridImageLayout(parent,files);
    imageLayout->addLayout(gridLayout->getGridLayout());
    QScrollArea *scroll = ui->scrollArea;
    scroll->setLayout(gridLayout);
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

