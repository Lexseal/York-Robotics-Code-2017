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

int n = 0;
Mat showImg;
Mat splitImg[4];
bool newImgAvai[4] = {0, 0, 0, 0};

void preProcess(const Mat& oriImg, Mat& threImg, const int& mode, const Scalar& low, const Scalar& high);
void splitPre(const int& myNum, const int& mode, const Scalar& low, const Scalar& high);
void show_Img();

static void onMouse (int event, int x, int y, int f, void *) {
	int H, S, V;
	Vec3b pix = showImg.at<Vec3b>(x, y);
	H = pix.val[0];
        S = pix.val[1];
        V = pix.val[2];

	cout << H << " " << S << " " << V << endl;
}

int main() {
    VideoCapture cap(0);
    cap.set(15, 1);
    cap.set(CV_CAP_PROP_FRAME_WIDTH, CAM_WIDTH);
    cap.set(CV_CAP_PROP_FRAME_HEIGHT, CAM_HEIGHT);
    
    Mat originalImg, thresholdedImg;
    
    if (!cap.isOpened()) {
        cout << "no cam available" << endl;
        //sharedPrint.sharedPrintln(stringstream("no cam available"));
        return -1;
    }
    cap.read(showImg);
    namedWindow("Show Img");
    namedWindow("original Img");
//thread tShow(show_Img);
    //tShow.join();
    
    Scalar lowHSV = cv::Scalar(55, 40, 205);
    Scalar highHSV = cv::Scalar(90, 255, 255);
    
     /*namedWindow("1");
     namedWindow("2");
     namedWindow("3");
     namedWindow("4");*/
    
    thread tPre[4];
    for (int i = 0; i < 4; i++) {
        tPre[i] = thread(splitPre, i, CV_BGR2HSV, lowHSV, highHSV);
    }
    
    //int n = 0;
    while (cap.read(originalImg)) {

        /*splitImg[0] = originalImg(Range(0, originalImg.rows/2-1), Range(0, originalImg.cols/2-1));
        splitImg[1] = originalImg(Range(0, originalImg.rows/2-1), Range(originalImg.cols/2, originalImg.cols-1));
        splitImg[2] = originalImg(Range(originalImg.rows/2, originalImg.rows-1), Range(0, originalImg.cols/2-1));
        splitImg[3] = originalImg(Range(originalImg.rows/2, originalImg.rows-1), Range(originalImg.cols/2, originalImg.cols-1));

	int b = 0, g = 0, r = 0;

        b = splitImg[1].at<Vec3b>(Point(10, 10)).val[0];
	g = splitImg[1].at<Vec3b>(Point(10, 10)).val[1];
	r = splitImg[1].at<Vec3b>(Point(10, 10)).val[2];

	cout << b << " " << g << " " << r << endl;

        for (int i = 0; i < 4; i++) {
            newImgAvai[i] = true;
        }
        
        while (newImgAvai[0] || newImgAvai[1] || newImgAvai[2] || newImgAvai[3]) {
        }*/
        
        //TODO
        
        preProcess(originalImg, thresholdedImg, CV_BGR2HSV, lowHSV, highHSV);

	//cvtColor(originalImg, showImg, CV_BGR2HSV);
	/*n++;
	if (!(n % 100)) {
		stringstream stream;
        	stream << "img" << n/100 << ".jpg";
        	imwrite(stream.str(), originalImg);
	}*/

        showImg = thresholdedImg;
	imshow("original Img", originalImg);
        imshow("Show Img", showImg);
	//setMouseCallback("Show Img", onMouse, 0);
        /*imshow("1", splitImg[0]);
        imshow("2", splitImg[1]);
        imshow("3", splitImg[2]);
        imshow("4", splitImg[3]);*/

        waitKey(20);
        
        //sharedPrint.sharedPrintln(stringstream() << n);
        //n++;
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
            cvtColor(splitImg[myNum], splitImg[myNum], mode);
            GaussianBlur(splitImg[myNum], splitImg[myNum], Size(7, 7), 2, 2);
            inRange(splitImg[myNum], low, high, splitImg[myNum]);
            newImgAvai[myNum] = false;
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
        //imshow("Show Img", showImg);
        //waitKey(30);
    }
}
