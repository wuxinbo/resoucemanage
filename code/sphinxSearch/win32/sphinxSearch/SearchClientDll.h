#pragma once
#include "pch.h"

/*
*  ��ʼ���ͻ���
*/
dllExport SearchClient* initClient(const char* host, int port);

/*
  �ؼ��ֲ�ѯ
*/
dllExport sphinx_result* queryByKeyword(PCSTR keyword, PCSTR index);
