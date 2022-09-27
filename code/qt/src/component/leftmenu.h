#ifndef LEFTMENU_H
#define LEFTMENU_H
#include "../common.h"
#include <QWidget>
namespace NAME_SPACE
{
     class LeftMenuPrivate;

    // 左侧菜单区域
    class LeftMenu : public QWidget
    {
        Q_OBJECT
    public:
        explicit LeftMenu(QWidget *parent = nullptr);
        ~LeftMenu();
//    signals:
    protected:
        QScopedPointer<LeftMenuPrivate> d_ptr;
    private:
        Q_DECLARE_PRIVATE(LeftMenu)
//        LeftMenuPrivate * d_ptr;
    };
}
#endif // LEFTMENU_H
