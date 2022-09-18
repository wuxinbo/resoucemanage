#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QVBoxLayout>
#include "src/component/plainedit.h"
#include <QPainter>
#include "ui_mainwindow.h"
#include <qstackedwidget.h>
#include "common.h"
#include "./component/leftmenu.h"
#include "./network/TcpServer.h"
class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();
public slots:
    /**
     * @brief 按钮点击事件
     */
    void buttonClick();
private:
    Ui::MainWindow *ui;
    /**
     *
     * @brief 初始化输入框，添加搜索按钮
     */
    void initInput();

    void initImageLayout();
    void paintEvent(QPaintEvent *event) ;
    void resizeEvent(QResizeEvent *event)override;
    void mousePressEvent(QMouseEvent *event);
    void mouseMoveEvent(QMouseEvent *event);
    void mouseReleaseEvent(QMouseEvent *event);
    void initMaxButton();
    /**
     * @brief 绘制边框阴影
     * @param painter
     */
    void drawShadow(QPainter &painter,QPaintEvent *event);
    //初始化左侧列表
    void initLeftList();
    /**
     * @brief 窗口左上角到鼠标之间的距离
     */
    QPoint leftToMouse;
    //相册布局
    QVBoxLayout *imageLayout;
    /**
     * 左侧菜单
     */
     NAME_SPACE::LeftMenu *leftMenu;

};
#endif // MAINWINDOW_H
