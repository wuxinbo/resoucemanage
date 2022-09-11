#include "leftmenu.h"
#include "ListView.h"
#include <QList>
class NAME_SPACE::LeftMenuPrivate {
private:
    ListView * listView;
    LeftMenu *q_ptr;
    Q_DECLARE_PUBLIC(LeftMenu)
public:
    LeftMenuPrivate(LeftMenu *q_ptr):q_ptr(q_ptr){
        listView = new NAME_SPACE::ListView(q_ptr->parentWidget());
        listView->setMinimumHeight(q_ptr->height());
    }
    ~LeftMenuPrivate(){
        delete listView;
    }
    void addItem(QListWidgetItem *item){
        listView->addItem(item);
    }
};

NAME_SPACE::LeftMenu::LeftMenu(QWidget *parent) : QWidget(parent),
    d_ptr(new NAME_SPACE::LeftMenuPrivate(this))
{
    this->setGeometry(frameGeometry());
    this->setMinimumHeight(100);
    //
    auto* photo =new QListWidgetItem{tr("photo")};
    auto* video =new QListWidgetItem{tr("video")};
    d_ptr->addItem(photo);
    d_ptr->addItem(video);
}

NAME_SPACE::LeftMenu::~LeftMenu()
{

}
