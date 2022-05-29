#include "plainedit.h"
#include <QPainter>
PlainEdit::PlainEdit(QWidget *parent)
    : QWidget{parent}
{
    resize(100,100);
    setMinimumSize(MIN_WIDTH,MIN_HEIGHT);
    setFocusPolicy(Qt::ClickFocus);
}

void PlainEdit::paintEvent(QPaintEvent *event){
    QPainter painter(this);
    qDebug("paint");
    QColor textColor(0, 127, 127, 191);
    QColor backGroundColor(0,0,0);
    painter.setPen(textColor);
    painter.setFont(QFont("华文宋体",12));
    painter.drawText(10,10,"你好啊");
    painter.drawRoundedRect(QRectF(0,0,MIN_WIDTH,MIN_HEIGHT),RADIUS,RADIUS);
    painter.setBackground(backGroundColor);


}

void PlainEdit::mousePressEvent(QMouseEvent *event){
    qDebug("mousePress");
}
