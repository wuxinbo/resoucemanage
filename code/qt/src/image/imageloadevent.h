#ifndef IMAGELOADEVENT_H
#define IMAGELOADEVENT_H
#include <QEvent>
//#include "imageViewer.h"
class ImageLoadEvent : public QEvent
{
public:
    /**
     * @brief 事件Id
     */
    const static int IMAGE_EVENT =QEvent::User+1;
    //ImageLoadEvent(ImageViewer* imageViewer);
    /**
     * @brief getImageviewer
     * @return
     */
    //ImageViewer* getImageviewer();


private:
    //ImageViewer* imageviewer;

};

#endif // IMAGELOADEVENT_H
