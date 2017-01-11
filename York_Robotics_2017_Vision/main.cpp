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
#include <unistd.h>
#include "SharedPrint.cpp"
#include "sockCom.hpp"

using namespace std;
using namespace cv;

#define CAM_WIDTH 640
#define CAM_HEIGHT 480

Mat showImg;
Mat splitImg[4];
bool newImgAvai[4] = {0, 0, 0, 0};

void preProcess(const Mat& oriImg, Mat& threImg, const int& mode, const Scalar& low, const Scalar& high);
void splitPre(const int& myNum, const int& mode, const Scalar& low, const Scalar& high);
void show_Img();

int main() {
    VideoCapture cap(0);
    cap.set(CV_CAP_PROP_FRAME_WIDTH, CAM_WIDTH);
    cap.set(CV_CAP_PROP_FRAME_HEIGHT, CAM_HEIGHT);
    
    Mat originalImg, thresholdedImg(CAM_HEIGHT, CAM_WIDTH, CV_8UC3);
    
    if (!cap.isOpened()) {
        sharedPrint.sharedPrintln(stringstream("no cam available"));
        return -1;
    }
    cap.read(originalImg);
    cap.read(thresholdedImg);
    cap.read(showImg);
    namedWindow("Show Img");
    //thread tShow(show_Img);
    //tShow.join();
    
    Scalar lowHSV = cv::Scalar(40, 100, 220);
    Scalar highHSV = cv::Scalar(140, 255, 255);
    
    /*namedWindow("1");
     namedWindow("2");
     namedWindow("3");
     namedWindow("4");*/
    
    thread tPre[4];
    for (int i = 0; i < 4; i++) {
        tPre[i] = thread(splitPre, i, CV_BGR2GRAY, lowHSV, highHSV);
    }
    
    int n = 0;
    while (cap.read(originalImg)) {
        splitImg[0] = originalImg(Range(0, originalImg.rows/2-1), Range(0, originalImg.cols/2-1));
        splitImg[1] = originalImg(Range(0, originalImg.rows/2-1), Range(originalImg.cols/2, originalImg.cols-1));
        splitImg[2] = originalImg(Range(originalImg.rows/2, originalImg.rows-1), Range(0, originalImg.cols/2-1));
        splitImg[3] = originalImg(Range(originalImg.rows/2, originalImg.rows-1), Range(originalImg.cols/2, originalImg.cols-1));
        
        for (int i = 0; i < 4; i++) {
            newImgAvai[i] = true;
        }
        
        while (newImgAvai[0] || newImgAvai[1] || newImgAvai[2] || newImgAvai[3]) {
        }
        
        
        
        //preProcess(originalImg, thresholdedImg, CV_BGR2GRAY, lowHSV, highHSV);
        
        showImg = thresholdedImg;
        //imshow("Show Img", showImg);
        imshow("1", splitImg[0]);
        imshow("2", splitImg[1]);
        imshow("3", splitImg[2]);
        imshow("4", splitImg[3]);
        waitKey(10);
        
        sharedPrint.sharedPrintln(stringstream() << n);
        n++;
    }
    
    for (int i = 0; i < 4; i++) {
        tPre[i].join();
    }
    
    return 0;
}

void preProcess(const Mat& oriImg, Mat& threImg, const int& mode, const Scalar& low, const Scalar& high)
{
    cvtColor(oriImg, threImg, mode);
    GaussianBlur(threImg, threImg, Size(7, 7), 2, 2);
    inRange(threImg, low, high, threImg);
}

void splitPre(const int& myNum, const int& mode, const Scalar& low, const Scalar& high)
{
    while (true) {
        if (newImgAvai[myNum]) {
            newImgAvai[myNum] = false;
            cvtColor(splitImg[myNum], splitImg[myNum], mode);
            GaussianBlur(splitImg[myNum], splitImg[myNum], Size(7, 7), 2, 2);
            inRange(splitImg[myNum], low, high, splitImg[myNum]);
        } else {
            usleep(200);
        }
    }
}

void show_Img()
{
    cout << "threadJoined" << endl;
    while (!showImg.empty()) {
        cout << "showing" << endl;
        imshow("Show Img", showImg);
        waitKey(30);
    }
}
