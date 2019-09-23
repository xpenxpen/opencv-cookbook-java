package opencv2_cookbook.chapter08;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.opencv.opencv_core.KeyPointVector;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.global.opencv_features2d;
import org.bytedeco.opencv.opencv_features2d.FastFeatureDetector;
import org.bytedeco.opencv.global.opencv_imgcodecs;

/**
 * The example for section "Detecting FAST features" in Chapter 8, page 203.
 */
public class Ex4FAST {

    public static void main(String[] args) {

        // Read input image
        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/church01.jpg"), opencv_imgcodecs.IMREAD_COLOR);

        // Detect FAST features
        FastFeatureDetector ffd = FastFeatureDetector.create(
          40 /* threshold for detection */ ,
          true /* non-max suppression */ ,
          FastFeatureDetector.TYPE_9_16);
        KeyPointVector keyPoints = new KeyPointVector();
        ffd.detect(src, keyPoints);

        // Draw keyPoints
        Mat canvas = new Mat();
        opencv_features2d.drawKeypoints(src, keyPoints, canvas, new Scalar(255, 255, 255, 0), opencv_features2d.DEFAULT);
        OpenCVUtilsJava.show(canvas, "FAST Features");
    }

}
