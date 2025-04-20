#include "../net/client.h"
#include <string>
using std::string;


int main(int argc, char **argv)
{
    std::string addr("192.168.2.5:8080");
    // std::string str("hello,woeod,你好啊");
        NET::TcpClient client ; 
        string data("aiya hello,world,hahahbuild] 1 warning generated.[build] \n");
        data+=" warning generated.[build] [100%] Linking CXX executable server[build] [100%] Built \n";
        data+=" target server[build] [ 50%] Building CXX object net/CMakeFiles/client.dir/client.cpp.o[build]\n";
        data+=" /Volumes/Data/code/resource-manage/code/native/net/client.cpp:18:10: warning: the current\n";
        data+=" #pragma pack alignment value is modified in the included file [-Wpragma-pack]\n";
        client.sendUTFData(addr.c_str(),data.c_str());
        // sock.sendBytes((char*) &header, sizeof(header));
        // std::thread receive(receiveData, sock);
        // receive.join();

    return 0;
}