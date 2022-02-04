#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <iostream>
#include "image/imageViewer.h"
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
    //获取listview控件
    imageLayout =ui->imageLayout;
    ImageViewer* image =new ImageViewer(nullptr,"D:\\wallerpage\\IMG_0311(20220105-212615).jpg");
//    bool loadResult =image->loadFile();
    QLabel *qlabel=image->getImageLable();
    imageLayout->addWidget(qlabel);
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

