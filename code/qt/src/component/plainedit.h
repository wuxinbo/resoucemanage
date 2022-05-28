#ifndef PLAINEDIT_H
#define PLAINEDIT_H

#include <QWidget>

class PlainEdit : public QWidget
{
    Q_OBJECT


public:
    explicit PlainEdit(QWidget *parent = nullptr);

protected:
    const int RADIUS =5;
    const int MIN_WIDTH=200;
    const int MIN_HEIGHT=50;
    void paintEvent(QPaintEvent *event) ;
    void mousePressEvent(QMouseEvent *event);
signals:

};

#endif // PLAINEDIT_H
