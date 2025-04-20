cmake_minimum_required(VERSION 3.20.0)
set(projectName "core" )
set(filewatch "filewatch")
set(imageMagick "imageMagick")
set(net "net")
project(${projectName} C CXX)
set(CMAKE_CXX_STANDARD 14)


include_directories(
    "${CMAKE_CURRENT_SOURCE_DIR}/include"
        "${CMAKE_CURRENT_SOURCE_DIR}/base"
    "${CMAKE_CURRENT_SOURCE_DIR}/include/jni"
)
# 设置include path
if(APPLE)
set(JAVA_INCLUDE "/usr/local/include/java")
include_directories( "${JAVA_INCLUDE}" "${JAVA_INCLUDE}/darwin")
endif()
if(WIN32)
include_directories(    
    "${CMAKE_CURRENT_SOURCE_DIR}/include/win32"
    "$ENV{JAVA_HOME}/include" 
    "$ENV{JAVA_HOME}/include/win32"
)
endif()
add_subdirectory(net)
message("开始构建")
if (APPLE)
   # 设置安装后的 rpath 为相对路径（相对于库自身位置）
   set(CMAKE_INSTALL_RPATH "@loader_path/../lib;/usr/local/lib")
   set(CMAKE_MACOSX_RPATH ON)  # 启用 macOS 的 rpath 支持
    # enable_testing()
    # add_subdirectory(test)
    target_link_libraries(${projectName} PUBLIC ${net}  )
    install(TARGETS ${projectName} DESTINATION "/Users/wuxinbo/Library/Java/Extensions")
endif()
if (LINUX)
    add_subdirectory(linux)
    include_directories("./include/linux")
    add_executable("watch" "filewatchTest.c" linux)
endif()
if(ANDROID)
    message("build android core  ")
    set(CMAKE_DEBUG_POSTFIX "")
    add_library(${projectName} SHARED  )
    target_link_libraries(${projectName} PUBLIC ${net}  )
endif(ANDROID)

if(WIN32)
    message("构建 windows dll")
    target_compile_definitions(${projectName} PRIVATE XBWUC_NET_EXPORTS)
    install(TARGETS ${projectName} DESTINATION "${CMAKE_CURRENT_SOURCE_DIR}/lib")
    # 在父模块的 CMakeLists.txt 中
    add_subdirectory(${imageMagick})
    add_subdirectory(${filewatch})
    #target_link_directories(${projectName} PUBLIC "./imageMagick/lib")
    target_link_libraries(${projectName} PRIVATE ${imageMagick} ${filewatch} ${net} )
  
endif()
