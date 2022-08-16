#pragma once
#include "pch.h"

/*
*  初始化客户端
*/
dllExport SearchClient* initClient(const char* host, int port);

/*
  关键字查询
*/
dllExport sphinx_result* queryByKeyword(PCSTR keyword, PCSTR index);
