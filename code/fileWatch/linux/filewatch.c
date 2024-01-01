#include "sys/inotify.h"
#include "stdio.h"
#include "unistd.h"
#include "stdbool.h"
#include "memory.h"
#include "com_wuxinbo_resourcemanage_jni_FileWatch.h"

//缓冲区长度
const short DATA_LENGTH=1024;

typedef struct inotify_event notifyEvent;
//实现文件监控
JNIEXPORT jobject JNICALL Java_com_wuxinbo_resourcemanage_jni_FileWatch_watchDir
  (JNIEnv *, jobject, jstring){


  }


void main (){

    int fd =inotify_init();
    int wd =inotify_add_watch(fd,"/home/wuxinbo/c_code/",IN_ALL_EVENTS);
    char data[DATA_LENGTH];
    printf("start monitor\n");
    memset(data,0,DATA_LENGTH);
    //监听文件修改事件
    while (true)
    {
        int readLength = read(fd,data,DATA_LENGTH);
        if(readLength>0){
            notifyEvent *event ;
            event =(notifyEvent *)data;
            if (event->len==0)
            {
                printf("only on event \n");
            }
            if(event->mask ==IN_CREATE){ //子文件创建
              printf("%s is create\n",event->name);  
            }else if (event->mask ==IN_ACCESS){ //子文件访问
                printf("%s access\n",event->name);  
            }else if(event->mask ==IN_DELETE){ //文件删除
                printf("%s delete\n",event->name); 
            }else if(event->mask == IN_MODIFY){ // 文件内容修改
                printf("%s modify\n",event->name); 
            }
            
        }
        /* code */
    }
    
    

}
