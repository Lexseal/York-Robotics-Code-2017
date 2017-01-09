//
//  sockCom.hpp
//  SocketComm
//
//  Created by Lexseal Lin on 9/8/16.
//  Copyright © 2016 Lexseal Lin. All rights reserved.
//

#ifndef sockCom_hpp
#define sockCom_hpp

#include <iostream>
#include <netinet/in.h>
#include <sys/socket.h>
#include <sstream>
#include "SharedPrint.cpp"

using namespace std;

class SocketServer
{
public:
    SocketServer(const u_short&);
    void sendStuff(const string&);
    bool connected;
    
private:
    int socketFd, socketConnect;
};

#endif /* sockCom_hpp */
