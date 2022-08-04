#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QVBoxLayout>
#include "src/component/plainedit.h"
QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

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

    void resizeEvent(QResizeEvent *event)override;
    void mousePressEvent(QMouseEvent *event);
    void mouseMoveEvent(QMouseEvent *event);
    void mouseReleaseEvent(QMouseEvent *event);
    void initMaxButton();
    /**
     * @brief 窗口左上角到鼠标之间的距离
     */
    QPoint leftToMouse;
    //相册布局
    QVBoxLayout *imageLayout;
    /**
     * 自定义控件
     */
    PlainEdit *edit;
};
#endif // MAINWINDOW_H
