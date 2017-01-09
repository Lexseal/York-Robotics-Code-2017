//
//  SharedPrint.hpp
//  OpenCV project 1
//
//  Created by Lexseal Lin on 9/3/16.
//  Copyright © 2016 Lexseal Lin. All rights reserved.
//

#ifndef SharedPrint_cpp
#define SharedPrint_cpp

#include <iostream>
#include <thread>
#include <mutex>
#include <sstream>

using namespace std;

class shared_Print
{
public:
    mutex lock;
    
    void sharedPrint(const stringstream& receive)
    {
        lock_guard<mutex> guard(lock);
        
        cout << receive.str();
    }
        
    void sharedPrintln(const stringstream& receive)
    {
        lock_guard<mutex> guard(lock);
            
        cout << receive.str() << endl;
    }
}sharedPrint;

#endif /* SharedPrint_cpp */
