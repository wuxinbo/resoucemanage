QT       += core gui concurrent

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

CONFIG += c++11

# You can make your code fail to compile if it uses deprecated APIs.
# In order to do so, uncomment the following line.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

SOURCES += \
    src/component/ListView.cpp \
    src/component/plainedit.cpp \
    src/component/leftmenu.cpp \
    src/component/rightcontent.cpp \
    src/image/gridImageLayout.cpp \
    src/image/imageViewer.cpp \
    src/image/imageloadevent.cpp \
    src/main.cpp \
    src/mainwindow.cpp \
    src/network/TcpServer.cpp

HEADERS += \
    src/common.h \
    src/component/leftmenu.h \
    src/component/ListView.h \
    src/component/plainedit.h \
    src/component/rightcontent.h \
    src/image/GridImageLayout.h \
    src/image/imageViewer.h \
    src/image/imageloadevent.h \
    src/mainwindow.h \
    src/network/TcpServer.h

FORMS += \
    detail.ui \
    src/image.ui \
    src/mainwindow.ui

TRANSLATIONS += \
    resource_zh_CN.ts
CONFIG += lrelease
CONFIG += embed_translations
# Default rules for deployment.
qnx: target.path = /tmp/$${TARGET}/bin
else: unix:!android: target.path = /opt/$${TARGET}/bin
!isEmpty(target.path): INSTALLS += target
RESOURCES += \
    resource.qrc

win32:CONFIG(release, debug|release): LIBS += -L$$PWD/lib/ -levent -lws2_32​
else:win32:CONFIG(debug, debug|release): LIBS += -L$$PWD/lib/ -levent -lws2_32​

INCLUDEPATH += $$PWD/src/include/libevent
DEPENDPATH += $$PWD/src/include/libevent
