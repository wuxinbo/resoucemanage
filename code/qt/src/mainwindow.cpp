#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <iostream>
#include "image/GridImageLayout.h"
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
    QList<QString> filePaths ={"E:\\Seafile\\photo\\2022\\01\\13\\export\\DSC_2147.jpg",
                               "E:\\Seafile\\photo\\wallerpage\\DSC_1658.jpg",
                               "E:\\Seafile\\photo\\wallerpage\\IMG_0101.jpg"};
    GridImageLayout *gridLayout =new GridImageLayout(filePaths);
    imageLayout->addLayout(gridLayout->getGridLayout());
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

