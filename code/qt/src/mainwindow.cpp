

#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "image/GridImageLayout.h"
#include <QDir>
#include <QStyle>
#include <QLabel>
#include <QAction>
#include <QIcon>
#include <QPixmap>
#include <QScreen>
#include <memory>
#include "./component/leftmenu.h"
MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    // 设置窗口最大化
    //showMaximized();
    setWindowFlag(Qt::WindowType::FramelessWindowHint);
   // setAttribute(Qt::WidgetAttribute::WA_TranslucentBackground);
    initInput();
    initMaxButton();
    initLeftList();
    //获取图片网格
    imageLayout =new QVBoxLayout(this);
    QDir dir("D:\\wallerpage");
    //获取目录内容
    QFileInfoList files=dir.entryInfoList();
    for(QFileInfo file:files){
        qDebug("fileName is:"+file.absoluteFilePath().toUtf8());
    }
    //std::shared_ptr<QLabel> title(new QLabel(QStringLiteral("2022年1月")));
//    imageLayout->addWidget(title.get());
    GridImageLayout *gridLayout =new GridImageLayout(parent,files);
    imageLayout->addLayout(gridLayout->getGridLayout());
    ui->scrollAreaWidgetContents->setLayout(imageLayout);
    //启动tcpserver
    NAME_SPACE::TcpServer* tcpServer = new NAME_SPACE::TcpServer();
}

void MainWindow::initInput(){
    

}

void MainWindow::paintEvent(QPaintEvent *event)
{
    QPainter painter(this) ;
    painter.setRenderHint(QPainter::RenderHint::Antialiasing);
    //drawShadow(painter,event);
}
void MainWindow::resizeEvent(QResizeEvent *event){
    QSize newSize =event->size();
    QString size ="resize "+QString::number(newSize.width());
    //qDebug(size);
    ui->scrollAreaWidgetContents->resize(newSize);
    //ui->scrollArea->resize(newSize);

}

void MainWindow::mousePressEvent(QMouseEvent *event)
{
    //获取鼠标的位置
    QPoint topLeft =this->geometry().topLeft();
    leftToMouse=event->globalPos()-topLeft ;
}

void MainWindow::mouseMoveEvent(QMouseEvent *event)
{
    QPoint topLeft =event->globalPos()-leftToMouse;
    this->move(topLeft);
}

void MainWindow::mouseReleaseEvent(QMouseEvent *event)
{
}

void MainWindow::initMaxButton()
{
    connect(ui->max,&QPushButton::clicked,this,[&]{
        if(!this->isMaximized()){
            ui->max->setIcon(QIcon(QPixmap(":/images/max2.png")));
            showMaximized();
        }else{
            ui->max->setIcon(QIcon(QPixmap(":/images/max.png")));
            showNormal();

        }

    });
}

void MainWindow::drawShadow(QPainter &painter,QPaintEvent *event)
{
    QColor pathColor("#CFCFD0");
//    painter.setBrush(pathColor);
    QPainterPath path;
    path.setFillRule(Qt::WindingFill);
    const QRect border = event->rect();
    path.addRect(border);
    //painter.fillPath(path,QBrush(pathColor));

//    path.moveTo(0,0); //原点
//    path.lineTo(border.right(), 0); //上
//    path.lineTo(border.right(),border.height()); //右
//    path.lineTo(0,border.height()); //下
//    path.lineTo()
    //painter.drawPath(path);
} 
void MainWindow::initLeftList()
{
    leftMenu = new xbwuc::LeftMenu(this);
}
MainWindow::~MainWindow()
{
    delete ui;
    delete imageLayout;
    delete leftMenu;
}
/**
 * @brief 按钮点击事件监听
 */
void MainWindow::buttonClick(){

}

