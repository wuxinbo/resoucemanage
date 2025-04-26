#include <mutex>
#include "common.h"
#include "MagickWand/MagickWand.h"
extern std::mutex* magick_wands_mutex;


XBWUC_NAMESPACE_START
class ImageMagick {
private:
  static const int MAX_SIZE = 10;
  static std::mutex *getMagickWandsMutex() ;

  static void put(MagickWand *wand) ;

  static MagickWand *get() ;

public:
  static inline const char *getTAG() ;
  static void initMaickWand();
  static int resize(const char * srcPathStr, const char *destPathStr,
                    int scale);
  static void destroy() ;
};
XBWUC_NAMESPACE_END