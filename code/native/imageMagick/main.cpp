
#include "MagickWand/MagickWand.h"
#include <iostream>
int main(int argc, char **argv)
{
  char *default_arg[] = {"", "D:\\wallerpage\\DSC_2984.jpg", ".\\test.jpg"};
  std::cout << "Hello World" << std::endl;
  MagickBooleanType status;
  MagickWand *magick_wand;
  /*
    Read an image.
  */
  MagickWandGenesis();
  magick_wand = NewMagickWand();
  status = MagickReadImage(magick_wand, default_arg[1]);
  if (status == MagickFalse)
  {
    std::cout << "Read image failed" << std::endl;
    exit(0);
  }
  //   ThrowWandException(magick_wand);
  int height =MagickGetImageHeight(magick_wand);
  int width = MagickGetImageWidth(magick_wand);
  std::cout << "Height: " << height << " Width: " << width << std::endl;
  MagickResetIterator(magick_wand);
  while (MagickNextImage(magick_wand) != MagickFalse)
  {
    MagickResizeImage(magick_wand, width/10, height/10, LanczosFilter);
  }
  status = MagickWriteImages(magick_wand, default_arg[2], MagickTrue);
  magick_wand = DestroyMagickWand(magick_wand);
  MagickWandTerminus();
  return (0);
}