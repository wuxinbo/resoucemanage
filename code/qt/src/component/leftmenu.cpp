#include "leftmenu.h"
#include "ListView.h"

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
    void addItem(ListItem *item){
        listView->addItem(item);
    }
};

NAME_SPACE::LeftMenu::LeftMenu(QWidget *parent) : QWidget(parent),
    d_ptr(new NAME_SPACE::LeftMenuPrivate(this))
{
    this->setMinimumHeight(parent->height());
    //
    auto* photo =new NAME_SPACE::ListItem{tr("photo")};
    auto* video =new NAME_SPACE::ListItem{tr("video")};
    d_ptr->addItem(photo);
    d_ptr->addItem(video);
}

NAME_SPACE::LeftMenu::~LeftMenu()
{

}
