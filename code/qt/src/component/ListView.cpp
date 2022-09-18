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
   static const int DEFAULT_WIDIH= 150;
   //字体大小
   static const int FONT_SIZE = 14;

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
    QFont font;
    font.setPointSize(ListViewPrivate::FONT_SIZE);
    this->setFont(font);
//    this->
    this->setMinimumHeight(parent->height());
    this->setMinimumWidth(ListViewPrivate::DEFAULT_WIDIH);
    this->setMaximumWidth(ListViewPrivate::DEFAULT_WIDIH + 300);
    // 绑定item点击事件
    connect(this,&ListView::itemClicked,this,&ListView::itemClick);
    connect(this,&ListView::currentItemChanged,this,&ListView::itemChanged);
}

xbwuc::ListView::~ListView()
{
    //清空列表

    delete d_ptr;

}

void xbwuc::ListView::itemClick(QListWidgetItem *item)
{

    qDebug("clicked ");
}

void xbwuc::ListView::itemChanged(QListWidgetItem *current, QListWidgetItem *prev)
{   
    //设置当前选中项的颜色
    current->setBackground(QColor("#007AFF"));
    if (prev) {
        prev->setBackgroundColor(QColor("#ffffff"));
    }
}

void NAME_SPACE::ListView::paintEvent(QPaintEvent *event)
{
    QListWidget::paintEvent(event);
    QPainter painter(viewport());
    painter.setRenderHint(QPainter::RenderHint::HighQualityAntialiasing);
    d_ptr->drawBorder(painter,this);
   // qDebug("update ListView");
    //绘制选中的背景色

//    int i=1;
//    //字体颜色
//    static QColor fontColor("#000000");
//    QFont font;
//    font.setFamily("Roboto");
//    font.setPointSize(14);
//    painter.setFont(font);
//    painter.setPen(fontColor);
}
