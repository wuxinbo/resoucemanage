#include "plainedit.h"
#include <QPainter>
#include <QFocusEvent>
PlainEdit::PlainEdit(QWidget *parent)
    : QPlainTextEdit(parent)
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
    painter.setRenderHint(QPainter::RenderHint::Antialiasing);
    drawBackground(painter);
    drawText(painter);
    if(focus){ //绘制焦点效果
       drawBorder(painter);
    }
}

void PlainEdit::mousePressEvent(QMouseEvent *event){
}

void PlainEdit::focusInEvent(QFocusEvent *event)
{
    focus = true;
    update();
}

void PlainEdit::focusOutEvent(QFocusEvent *event)
{
   focus=false;
   update();
}

void PlainEdit::drawBackground(QPainter &painter)
{

    QColor backGroundColor(255,255,255);
    painter.setPen(Qt::NoPen);
    painter.setBrush(backGroundColor);
    painter.drawRoundedRect(QRectF(0,0,MIN_WIDTH,MIN_HEIGHT),RADIUS,RADIUS);
}

void PlainEdit::drawText(QPainter &painter)
{
    QFont font("华文宋体",fontSize);
    QFontMetrics metrics(font);
    painter.setFont(font);
    painter.setPen(*textColor);
    painter.drawText(10,metrics.height(),QStringLiteral("你好啊"));
}

void PlainEdit::drawBorder(QPainter &painter)
{
    painter.setBrush(Qt::BrushStyle::NoBrush);
    painter.drawRoundedRect(QRectF(0,0,MIN_WIDTH,MIN_HEIGHT),RADIUS,RADIUS);
}
