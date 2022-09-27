#include "searchClient.h"
#include <iostream>
#if win32
#include <WinSock2.h>
#endif


void net_init()
{
#if _WIN32
	// init WSA on Windows
	WSADATA wsa_data;
	int wsa_startup_err;

	wsa_startup_err = WSAStartup(WINSOCK_VERSION, &wsa_data);
	if (wsa_startup_err) {
		std::cout << "failed to initialize WinSock2 " << std::endl;
	}
		//die("failed to initialize WinSock2: error %d", wsa_startup_err);
#endif
}

SearchClient::SearchClient(
	const char* host,
	int port )
{
	client =sphinx_create(false);
	//��������client host��port��
	sphinx_set_server(client,host,port);
	net_init();

}
SearchClient::~SearchClient()
{
	delete client;

}
sphinx_result* SearchClient::query(const char * keyword,const char * index)
{
	sphinx_result *res = sphinx_query(client, keyword, index, 0);
	if (!res) {
		std::cout << "query fail" << std::endl;
	}

	return res;
}
