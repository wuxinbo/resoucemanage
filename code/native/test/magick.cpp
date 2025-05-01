
#include "../imageMagick/imageMagick.h"
#include <iostream>
int main(int argc, char **argv)
{
  char *default_arg[] = {"", "D:\\wallerpage\\DSC_3224.jpg", "D:\\wallerpage\\test.jpg"};
  std::cout << "Hello World" << __FUNCSIG__<< std::endl;
  xbwuc::ImageMagick::initMaickWand();
  xbwuc::ImageMagick::resize(default_arg[1], default_arg[2], 10);

  return (0);
}