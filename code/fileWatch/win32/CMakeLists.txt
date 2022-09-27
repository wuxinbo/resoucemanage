cmake_minimum_required(VERSION 3.20.0)

project(filewatch)
set(CMAKE_CXX_STANDARD 11)

include_directories(../include ../include/win32)
add_library(filewatch SHARED fileWatch.cpp)