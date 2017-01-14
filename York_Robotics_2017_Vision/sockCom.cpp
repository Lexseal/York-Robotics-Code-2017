//
//  sockCom.cpp
//  SocketComm
//
//  Created by Lexseal Lin on 9/8/16.
//  Copyright © 2016 Lexseal Lin. All rights reserved.
//

#include "sockCom.hpp"

SocketServer::SocketServer(const u_short& portNum)
{
    struct sockaddr_in _sockAddr;
    _sockAddr.sin_family = AF_INET;
    _sockAddr.sin_addr.s_addr = htons(INADDR_ANY);
    _sockAddr.sin_port = htons(portNum);
    socklen_t size = sizeof(_sockAddr);
    
    if ((socketFd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        cout << "\nError establishing socket..." << endl;
        throw "error0";
    } else {
        cout << "\n=> Socket server has been created..." << endl;
    }
    
    if ((::bind(socketFd, (struct sockaddr*)&_sockAddr, size)) < 0) {
        cout << "=> Error binding connection, the socket has already been established..." << endl;
        throw "error1";
    }
    
    cout << "=> Looking for clients..." << endl;
    listen(socketFd, 1);
    
    if ((socketConnect = accept(socketFd,(struct sockaddr *)&_sockAddr, &size)) < 0) {
        cout << "=> Error on accepting..." << endl;
        throw "error2";
    } else {
        cout << "=> Connected with the client # " << socketConnect << endl;
        connected = true;
    }
}

void SocketServer::sendStuff(const string& str)
{
    stringstream stream;
    stream << "$" << str << "*\n";
    
    int bufsize = 16;
    char buffer[bufsize];
    
    //strcpy(buffer, stream.str().c_str());
    
    send(socketConnect, buffer, bufsize, 0);
}
