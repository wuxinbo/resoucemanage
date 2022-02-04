QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

CONFIG += c++11

# You can make your code fail to compile if it uses deprecated APIs.
# In order to do so, uncomment the following line.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

SOURCES += \
    src/image/gridImageLayout.cpp \
    src/image/imageViewer.cpp \
    src/main.cpp \
    src/mainwindow.cpp

HEADERS += \
    src/image/GridImageLayout.h \
    src/image/imageViewer.h \
    src/mainwindow.h

FORMS += \
    src/mainwindow.ui

TRANSLATIONS += \
    resource_zh_CN.ts
CONFIG += lrelease
CONFIG += embed_translations

# Default rules for deployment.
qnx: target.path = /tmp/$${TARGET}/bin
else: unix:!android: target.path = /opt/$${TARGET}/bin
!isEmpty(target.path): INSTALLS += target
