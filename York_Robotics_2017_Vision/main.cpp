//
//  main.cpp
//  York_Robotics_2017_Vision
//
//  Created by Lexseal Lin on 1/9/17.
//  Copyright © 2017 Lexseal Lin. All rights reserved.
//

#include <iostream>
#include <opencv2/opencv.hpp>
#include <thread>
#include <mutex>
#include <sstream>
#include "SharedPrint.cpp"
#include "sockCom.hpp"

using namespace std;
using namespace cv;

#define CAM_WIDTH 640
#define CAM_HEIGHT 480

int main() {
    VideoCapture cap(0);
    cap.set(CV_CAP_PROP_FRAME_WIDTH, CAM_WIDTH);
    cap.set(CV_CAP_PROP_FRAME_HEIGHT, CAM_HEIGHT);
    
    Mat originalImg;
    
    if (!cap.isOpened()) {
        sharedPrint.sharedPrintln(stringstream("no cam available"));
        return -1;
    } else if (!cap.read(originalImg))
    {
        sharedPrint.sharedPrintln(stringstream("no cam img available"));
        return -1;
    }

    return 0;
}
