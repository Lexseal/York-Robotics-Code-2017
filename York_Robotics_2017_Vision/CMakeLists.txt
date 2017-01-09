cmake_minimum_required(VERSION 3.0)

project( yorkVision2017 )

find_package( OpenCV REQUIRED )
find_package( Threads REQUIRED )

add_library(SharedPrt SharedPrint.cpp)
add_library(SockCom sockCom.cpp)

add_executable( yorkVision2017 main.cpp )

target_link_libraries( yorkVision2017 ${OpenCV_LIBS} )
target_link_libraries( yorkVision2017 ${CMAKE_THREAD_LIBS_INIT} )
target_link_libraries( yorkVision2017 SharedPrt )
target_link_libraries( yorkVision2017 SockCom )

#INCLUDE_DIRECTORIES( /usr/loca/include )
#LINK_DIRECTORIES( /usr/local/lib )

include(CheckCXXCompilerFlag)
CHECK_CXX_COMPILER_FLAG("-std=c++1z" COMPILER_SUPPORTS_CXX1z)
CHECK_CXX_COMPILER_FLAG("-std=c++14" COMPILER_SUPPORTS_CXX14)
CHECK_CXX_COMPILER_FLAG("-std=c++11" COMPILER_SUPPORTS_CXX11)
CHECK_CXX_COMPILER_FLAG("-std=c++0x" COMPILER_SUPPORTS_CXX0X)
if(COMPILER_SUPPORTS_CXX1z)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++1z")
elseif(COMPILER_SUPPORTS_CXX14)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14")
elseif(COMPILER_SUPPORTS_CXX11)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
else()
    message(STATUS "The compiler ${CMAKE_CXX_COMPILER} has no c++17, c++14 nor C++11 support. Please use a different C++ compiler.")
endif()
