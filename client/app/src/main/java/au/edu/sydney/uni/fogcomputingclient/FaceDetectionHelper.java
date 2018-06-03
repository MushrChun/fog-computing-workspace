package au.edu.sydney.uni.fogcomputingclient;

import android.content.Context;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

public class FaceDetectionHelper {

    private static final String TAG = "FaceDetectionHelper";

    public CascadeClassifier mJavaDetector;
    public File mCascadeFile;

    public FaceDetectionHelper(){
        try {
            // load cascade file from application resources
//            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
//            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
//            mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
//            FileOutputStream os = new FileOutputStream(mCascadeFile);
//
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = is.read(buffer)) != -1) {
//                os.write(buffer, 0, bytesRead);
//            }
//            is.close();
//            os.close();

            mCascadeFile = new File("/storage/emulated/0/opencv/haarcascade_frontalface_alt.xml");

            mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            if (mJavaDetector.empty()) {
                Log.e(TAG, "Failed to load cascade classifier");
                mJavaDetector = null;
            } else
                Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
        }
    }

    public DetectionFrame[] detect(Mat gray, int absoluteFaceSize){
        MatOfRect faces = new MatOfRect();
        mJavaDetector.detectMultiScale(gray, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        Rect[] facesArray = faces.toArray();
        DetectionFrame[] frame = new DetectionFrame[facesArray.length];
        for(int i=0; i<facesArray.length; i++){

//            Log.i(TAG, "Face Rect: " + mCascadeFile.getAbsolutePath());
            DetectionFrame f = new DetectionFrame();
            f.x = facesArray[i].x;
            f.y = facesArray[i].y;
            f.h = facesArray[i].height;
            f.w = facesArray[i].width;
            f.label = "face";
            frame[i] = f;
        }
        return frame;
    }
}
