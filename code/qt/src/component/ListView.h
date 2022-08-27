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
    void addItem(ListItem * item);
//    void
protected:

    void paintEvent(QPaintEvent *event) override;

private:

    ListViewPrivate * d_ptr;
};


}


