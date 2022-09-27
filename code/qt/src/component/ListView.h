#pragma once
#include <qwidget.h>
#include <QListWidget>
#include "../common.h"
namespace NAME_SPACE {

class ListViewPrivate;
struct ListItem{
    QString name;


};
// 菜单选项
class ListView :
    public QListWidget
{
    Q_OBJECT
public:
    ListView(QWidget *parent);
    ~ListView();
    /**
     * @brief 选项点击事件
     * @param item
     */
    void itemClick(QListWidgetItem *item);
    void itemChanged(QListWidgetItem *current,QListWidgetItem *prev);
//    void
protected:

    void paintEvent(QPaintEvent *event) override;

private:

    ListViewPrivate * d_ptr;
};


}


