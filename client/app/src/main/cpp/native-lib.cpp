#include <jni.h>
#include <string>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/features2d/features2d.hpp>
#include <opencv2/opencv.hpp>
#include <android/log.h>
#include <stdio.h>
#include <sstream>


std::string face_cascade_name = "/storage/emulated/0/opencv/haarcascade_frontalface_alt.xml";
std::string eyes_cascade_name = "/storage/emulated/0/opencv/haarcascade_eye_tree_eyeglasses.xml";

cv::CascadeClassifier face_cascade;
cv::CascadeClassifier eyes_cascade;

std::vector<cv::Rect>* detect(cv::Mat& frame) {
    if( face_cascade.empty() && !face_cascade.load(face_cascade_name)){
        printf("--(!)Error loading\n");
        __android_log_print(ANDROID_LOG_ERROR, "opencv", "Cascade Loading Fails!", 1);
        return NULL;
    }
    if( eyes_cascade.empty() && !eyes_cascade.load(eyes_cascade_name)){
        printf("--(!)Error loading\n");
        __android_log_print(ANDROID_LOG_ERROR, "opencv", "Cascade Loading Fails!", 1);
        return NULL;
    }

    std::vector<cv::Rect> * faces = new std::vector<cv::Rect>();
    cv::Mat frame_gray;
//    __android_log_print(ANDROID_LOG_ERROR, "opencv", "before color transfer", 1);
    cvtColor( frame, frame_gray, cv::COLOR_RGBA2GRAY );
    equalizeHist( frame_gray, frame_gray );
    //-- Detect faces
    face_cascade.detectMultiScale( frame_gray, *faces, 1.1, 2, 0|cv::CASCADE_SCALE_IMAGE, cv::Size(30, 30) );
    std::ostringstream ss;
    ss<<"face array size:"<<faces->size();
    std::string output = ss.str();
    __android_log_print(ANDROID_LOG_ERROR, "opencv", output.c_str(), 1);
    return faces;
//    for ( size_t i = 0; i < faces.size(); i++ )
//    {
//        cv::Point center( faces[i].x + faces[i].width/2, faces[i].y + faces[i].height/2 );
//        ellipse( frame, center, cv::Size( faces[i].width/2, faces[i].height/2 ), 0, 0, 360, cv::Scalar( 255, 0, 255 ), 4, 8, 0 );
//        cv::Mat faceROI = frame_gray( faces[i] );
//        std::vector<cv::Rect> eyes;
//        //-- In each face, detect eyes
//        eyes_cascade.detectMultiScale( faceROI, eyes, 1.1, 2, 0 |cv::CASCADE_SCALE_IMAGE, cv::Size(30, 30) );
//        for ( size_t j = 0; j < eyes.size(); j++ )
//        {
//            cv::Point eye_center( faces[i].x + eyes[j].x + eyes[j].width/2, faces[i].y + eyes[j].y + eyes[j].height/2 );
//            int radius = cvRound( (eyes[j].width + eyes[j].height)*0.25 );
//            circle( frame, eye_center, radius, cv::Scalar( 255, 0, 0 ), 4, 8, 0 );
//        }
//    }

}


extern "C"
JNIEXPORT jobjectArray

JNICALL Java_au_edu_sydney_uni_fogcomputingclient_CameraFragment_detect(
        JNIEnv *env,
        jlong addrRgba /* this */){
    cv::Mat& frame = * (cv::Mat*)addrRgba;
    std::vector<cv::Rect>* rects = detect(frame);
//    delete &frame;
    size_t rectsSize = rects->size();
    jsize rectsJsize = rectsSize;
    jclass detectionFrameClass = (*env).FindClass("au/edu/sydney/uni/fogcomputingclient/DetectionFrame");
    jmethodID detectionFrameClassInit = (*env).GetMethodID(detectionFrameClass, "<init>", "()V");
    jobjectArray frames = (*env).NewObjectArray(rectsJsize, detectionFrameClass, NULL);
    for(size_t i = 0; i< rectsSize; i++){
        std::ostringstream ss;
        ss << (*rects)[i].x << ":" << (*rects)[i].y;
        std::string output = ss.str();
        __android_log_print(ANDROID_LOG_ERROR, "opencv", output.c_str(), 1);
        jobject oneFrame = (*env).NewObject(detectionFrameClass, detectionFrameClassInit);
        jfieldID  detectionFrameClassX = (*env).GetFieldID(detectionFrameClass, "x", "I");
        jfieldID  detectionFrameClassY = (*env).GetFieldID(detectionFrameClass, "y", "I");
        jfieldID  detectionFrameClassW = (*env).GetFieldID(detectionFrameClass, "w", "I");
        jfieldID  detectionFrameClassH = (*env).GetFieldID(detectionFrameClass, "h", "I");
        jfieldID  detectionFrameClassLabel = (*env).GetFieldID(detectionFrameClass, "label", "Ljava/lang/String;");

        (*env).SetIntField(oneFrame, detectionFrameClassX, (*rects)[i].x);
        (*env).SetIntField(oneFrame, detectionFrameClassY, (*rects)[i].y);
        (*env).SetIntField(oneFrame, detectionFrameClassW, (*rects)[i].width);
        (*env).SetIntField(oneFrame, detectionFrameClassH, (*rects)[i].height);
        (*env).SetObjectField(oneFrame, detectionFrameClassLabel, (*env).NewStringUTF("face"));
        (*env).SetObjectArrayElement(frames, i, oneFrame);

    }

    return frames;
}


