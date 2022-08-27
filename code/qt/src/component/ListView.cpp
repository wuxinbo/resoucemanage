#include "ListView.h"
#include <QList>
#include <QString>
#include <QPainter>
//


class NAME_SPACE::ListViewPrivate{
public :
   QList<ListItem *> listItems;

    //item 默认高度
   static const int DEFAULT_HEIGHT =30;

   //绘制边框
   void drawBorder(QPainter &painter,NAME_SPACE::ListView *list){

   }

};


NAME_SPACE::ListView::ListView(QWidget *parent):
    QListWidget(parent),
    d_ptr(new ListViewPrivate)
{
    d_ptr->listItems = {};
    this->setStyleSheet("border:none");
}

xbwuc::ListView::~ListView()
{
    delete d_ptr;

}

void xbwuc::ListView::addItem(xbwuc::ListItem *item)
{
    d_ptr->listItems.push_back(item);

    //重新绘制
    update();
}

void NAME_SPACE::ListView::paintEvent(QPaintEvent *event)
{
    QListWidget::paintEvent(event);
    QPainter painter(viewport());
    painter.backgroundMode();
    d_ptr->drawBorder(painter,this);
    int i=1;
//    //字体颜色
    static QColor fontColor("#000000");
    QFont font;
    font.setFamily("Roboto");
    font.setPointSize(14);
    painter.setFont(font);
    painter.setPen(fontColor);

    for (ListItem* item : d_ptr->listItems){
        painter.drawText(this->width()/2-10,i*ListViewPrivate::DEFAULT_HEIGHT,item->name);
        i++;
    }
}
