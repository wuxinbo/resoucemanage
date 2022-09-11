#pragma once
#include "../common.h"

namespace NAME_SPACE {

	class TcpServerPrivate;
	/*
	* socket server ,use libevent
	*/
	class TcpServer
	{
	public:
		TcpServer();
		~TcpServer();
		void init();
	private:

		TcpServerPrivate* tcpServerPrivate;

	};

}


