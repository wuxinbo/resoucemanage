#include "plainedit.h"
#include <QPainter>
#include <QFocusEvent>
PlainEdit::PlainEdit(QWidget *parent)
    : QWidget{parent}
{
    resize(100,100);
    setMinimumSize(MIN_WIDTH,MIN_HEIGHT);
    setFocusPolicy(Qt::ClickFocus);
    textColor = new QColor(127, 127, 191);

}

PlainEdit::~PlainEdit()
{
    delete textColor;
    delete backgrouodColor;
    delete borderColor;
}

void PlainEdit::paintEvent(QPaintEvent *event){
    QPainter painter(this);
    qDebug("paint");
    painter.setRenderHint(QPainter::RenderHint::Antialiasing);
    QColor backGroundColor(255,255,255);
    painter.setPen(*textColor);
    QFont font("华文宋体",fontSize);
    QFontMetrics metrics(font);
    painter.setFont(font);
    painter.setBrush(backGroundColor);
    painter.drawRoundedRect(QRectF(0,0,MIN_WIDTH,MIN_HEIGHT),RADIUS,RADIUS);
    painter.drawText(10,metrics.height(),QStringLiteral("你好啊"));
    if(focus){ //绘制焦点效果
       painter.setBrush(Qt::BrushStyle::NoBrush);
       painter.drawRoundedRect(QRectF(0,0,MIN_WIDTH,MIN_HEIGHT),RADIUS,RADIUS);
    }
}

void PlainEdit::mousePressEvent(QMouseEvent *event){
}

void PlainEdit::focusInEvent(QFocusEvent *event)
{
    focus = true;
    update();
    qDebug("getFcous");
}

void PlainEdit::focusOutEvent(QFocusEvent *event)
{
   focus=false;
   update();
   qDebug("lostFcous");
}
