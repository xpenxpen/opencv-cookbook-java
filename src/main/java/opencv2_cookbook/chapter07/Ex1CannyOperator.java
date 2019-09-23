package opencv2_cookbook.chapter07;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;

/**
 * The example for section "Detecting image contours with the Canny operator" in Chapter 7, page 164.
 */
public class Ex1CannyOperator {

    public static void main(String[] args) {

        // Read input image
        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/road.jpg"), opencv_imgcodecs.IMREAD_GRAYSCALE);
        
        // Canny contours
        Mat contours = new Mat();
        int threshold1 = 125;
        int threshold2 = 350;
        int apertureSize = 3;
        opencv_imgproc.Canny(src, contours, threshold1, threshold2, apertureSize, true /*L2 gradient*/);
        
        OpenCVUtilsJava.show(contours, "Canny Contours");

    }

}
