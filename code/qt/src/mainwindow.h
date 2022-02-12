#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QVBoxLayout>
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
    //相册布局
    QVBoxLayout *imageLayout;
};
#endif // MAINWINDOW_H
