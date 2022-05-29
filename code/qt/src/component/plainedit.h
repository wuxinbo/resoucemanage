#ifndef PLAINEDIT_H
#define PLAINEDIT_H

#include <QWidget>
#include <QColor>

class PlainEdit : public QWidget
{
    Q_OBJECT


public:
    explicit PlainEdit(QWidget *parent = nullptr);
    ~ PlainEdit();

protected:
    /**
     * @brief 行高由字体计算得来
     */
    int lineHeight;
    /**
     * @brief 默认为12
     */
    int fontSize =12;
    const int RADIUS =5;
    const int MIN_WIDTH=200;
    const int MIN_HEIGHT=50;

    void paintEvent(QPaintEvent *event) ;
    void mousePressEvent(QMouseEvent *event);
    void focusInEvent(QFocusEvent *event) override;
    void focusOutEvent(QFocusEvent *event) override;
private:
    QColor *textColor;
    QColor *backgrouodColor;
    QColor *borderColor;

    bool  focus;
signals:

};

#endif // PLAINEDIT_H
