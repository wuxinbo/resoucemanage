cmake_minimum_required(VERSION 3.20.0)

#project(filewatch)
include_directories(../include ../include/linux )

add_library(filewatch SHARED filewatch.c )