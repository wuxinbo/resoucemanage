#include "TcpServer.h"
#include "event2/event.h"
#include <signal.h>
#include <event2/listener.h>
#include <event2/bufferevent.h>
#include <event2/buffer.h>
#include <QtConcurrent/QtConcurrent>
#include <iostream>

#ifdef _WIN32
#pragma comment(lib,"ws2_32.lib")
#endif
namespace NAME_SPACE {
	const char* welcome = "你好xbwuc";


	// 写数据 
	static void conn_writeData(struct bufferevent* event , void* data) {
		struct evbuffer* output = bufferevent_get_output(event);
		if (evbuffer_get_length(output) == 0) {
			printf("flushed answer\n");
			bufferevent_free(event);
		}
		//output.
	}
	//读取数据
	static void readDatacb(struct bufferevent* event, void* data) {
		struct evbuffer* input = bufferevent_get_input(event);
		int length =evbuffer_get_length(input);
		if ( length== 0) {
			printf("flushed answer\n");
			bufferevent_free(event);
		}
		char* readData =new char[length];
		bufferevent_read(event, readData, length);
		//事件处理

		bufferevent_write(event, readData, length);
		delete[] readData;
	}
	static void conn_eventcb(struct bufferevent*, short, void*) {
		
	}


	//连接监听方法
	void listenerFc(struct evconnlistener*, evutil_socket_t fd, struct sockaddr*, int socklen,  void* data) {
		struct event_base* base = static_cast<event_base*>(data);
		struct bufferevent* bev;

		bev = bufferevent_socket_new(base, fd, BEV_OPT_CLOSE_ON_FREE);
		if (!bev) {
			fprintf(stderr, "Error constructing bufferevent!");
			event_base_loopbreak(base);
			return;
		}
		bufferevent_setcb(bev, readDatacb, nullptr, conn_eventcb, NULL);
		bufferevent_enable(bev, EV_READ);
		bufferevent_write(bev,welcome,sizeof(welcome));
	}
	// 中断监听
	static void signal_cb(evutil_socket_t, short, void*) {
	
	}
	class TcpServerPrivate {
	public:
		TcpServerPrivate() {
			initServer();
		}
		~TcpServerPrivate() {
			event_base_free(base);
		}
		void initServer() {
#ifdef _WIN32
			WSADATA wsa_data;
			WSAStartup(0x0201, &wsa_data);
#endif
			//创建事件
			base =event_base_new();
			if (!base) {
				return;
			}
			sin.sin_family = AF_INET;
			sin.sin_port = port;
			listener = evconnlistener_new_bind(base, listenerFc, (void*)base,
				LEV_OPT_REUSEABLE | LEV_OPT_CLOSE_ON_FREE, -1,
				(struct sockaddr*)&sin,
				sizeof(sin));

			if (!listener) {
				fprintf(stderr, "Could not create a listener!\n");
				return ;
			}

			signal_event = evsignal_new(base, SIGINT, signal_cb, (void*)base);

			if (!signal_event || event_add(signal_event, NULL) < 0) {
				fprintf(stderr, "Could not create/add a signal event!\n");
				return ;
			}
			//在其他线程中进行事件分发
			QtConcurrent::run([&]{
				event_base_dispatch(base);
			});
		}
	private:
		//需要监听的端口
        const USHORT port = htons(8088);
		event_base *base;
		struct sockaddr_in sin = { 0 };
		struct evconnlistener* listener;
		struct event* signal_event;
		/*
		*  监听方法
		*/
		evconnlistener_cb listenerCb = listenerFc;
	

	};

	TcpServer::TcpServer():tcpServerPrivate(new TcpServerPrivate()) {
		
	}

	TcpServer::~TcpServer() {

		delete tcpServerPrivate;
	}

}
