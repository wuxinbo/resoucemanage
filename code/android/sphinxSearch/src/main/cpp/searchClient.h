#pragma once
#ifndef  SEARCH_CLIENT
#define  SEARCH_CLIENT

#endif // ! SEARCH_CLIENT

#include "sphinxclient.h"


class SearchClient {
public :
	SearchClient(const char* host ="192.168.2.3",
		int port =9312
	);
	/*
	* 查询数据
	*/
	sphinx_result* query(const char* keyword, const char* index);
	/* 真正的客户端
	*
	*/
	sphinx_client* client;
private:
	~SearchClient();
};