cmake_minimum_required(VERSION 3.20.0)
set(projectName "xbwuc_core" )
set(filewatch "xbwuc::filewatch")
set(imageMagick "xbwuc::imageMagick")
set(net "xbwuc::net")
set(base "xbwuc::base")
project(${projectName} C CXX)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_DEBUG_POSTFIX "")
set(CMAKE_RELEASE_POSTFIX "")
set(CMAKE_EXPORT_COMPILE_COMMANDS 1)
include_directories(
    "include"
    "${CMAKE_CURRENT_SOURCE_DIR}/base"
    "${CMAKE_CURRENT_SOURCE_DIR}/include/jni"
)
if(CMAKE_CXX_COMPILER_ID MATCHES "Clang")
    set(CMAKE_C_FLAGS_RELEASE "-O0 -fexceptions  -g")
    set(CMAKE_CXX_FLAGS_RELEASE  "-O0 -fexceptions -g")
    set(CMAKE_LINKER_FLAGS_RELEASE "-O0 -fexceptions  -g")
endif()
# 设置include path
if(APPLE)
set(JAVA_INCLUDE "/usr/local/include/java")
include_directories( "${JAVA_INCLUDE}" "${JAVA_INCLUDE}/darwin")
endif()

if(WIN32)
set(POCO_DIR "E:/poco-1.14.1-all")
include_directories(    
    "${CMAKE_CURRENT_SOURCE_DIR}/include/win32"
    "$ENV{JAVA_HOME}/include" 
    "$ENV{JAVA_HOME}/include/win32"
)
endif()
if(ANDROID)
    set(POCO_DIR "E:/poco-1.14.1-all")
endif()
list(APPEND CMAKE_PREFIX_PATH "${POCO_DIR}")
message("开始构建 CMAKE_PREFIX_PATH ${CMAKE_PREFIX_PATH}")
add_subdirectory(net)
add_subdirectory(base)
if (APPLE)
   # 设置安装后的 rpath 为相对路径（相对于库自身位置）
   set(CMAKE_INSTALL_RPATH "@loader_path/../lib;/usr/local/lib")
   set(CMAKE_MACOSX_RPATH ON)  # 启用 macOS 的 rpath 支持
    target_link_libraries(${projectName} PUBLIC ${base} ${net}  )
    install(TARGETS ${projectName} DESTINATION "/Users/wuxinbo/Library/Java/Extensions")
endif()
if (LINUX)
    add_subdirectory(linux)
    include_directories("./include/linux")
    add_executable("watch" "filewatchTest.c" linux)
endif()
if(ANDROID)
    message("build android core  ")
    add_library(${projectName} SHARED  )

    add_subdirectory( ${POCO_DIR} poco)
    target_link_libraries(${projectName}  PUBLIC xbwuc::base xbwuc::net
            Poco::Net log )
    install(TARGETS xbwuc_core DESTINATION "${CMAKE_CURRENT_SOURCE_DIR}/androidlib")
#    target_link_libraries(${projectName} PUBLIC ${base} ${net}  )
endif(ANDROID)

if(WIN32)
    message("构建 windows dll")
    add_subdirectory( ${POCO_DIR} poco)
    if(${BUILD_STATIC})
        add_library(${projectName} STATIC)
    else ()
        add_library(${projectName} SHARED  )
    endif()
    add_library("xbwuc::core" ALIAS xbwuc_core)
    if(NOT Poco_FOUND)
#    message(FATAL_ERROR "Poco not found!")
    else()
    message(STATUS "Poco found: ${Poco_DIR}")  # 输出 Poco 的 CMake 配置路径
    message(STATUS "Poco includes: ${Poco_INCLUDE_DIRS}")  # 检查头文件路径
    message(STATUS "Poco libraries: ${Poco_LIBRARIES}")     # 检查库路径
    endif()
    set(CMAKE_INSTALL_PREFIX "${CMAKE_SOURCE_DIR}/install")
    install(TARGETS xbwuc_core DESTINATION "${CMAKE_CURRENT_SOURCE_DIR}/lib")

    # 在父模块的 CMakeLists.txt 中
    add_subdirectory(imageMagick)
    add_subdirectory(filewatch)
    # enable_testing()
    # add_subdirectory(test)
    target_link_libraries(xbwuc_core  ${base} ${imageMagick} ${filewatch} ${net} )
  
endif()
