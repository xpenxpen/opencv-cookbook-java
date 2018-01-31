package opencv2_cookbook.chapter05;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * Example of using morphological opening and closing in chapter 5 section
 * "Opening and closing images using morphological filters".
 */
public class Ex2OpeningAndClosing {

    public static void main(String[] args) {

        // Read input image
        Mat image = OpenCVUtilsJava.loadAndShowOrExit(new File("data/binary.bmp"), opencv_imgcodecs.IMREAD_COLOR);
        
        // Create 5x5 structural element
        Mat element5 = new Mat(5, 5, opencv_core.CV_8U, new Scalar(1d));

        // Closing
        Mat closed = new Mat();
        opencv_imgproc.morphologyEx(image, closed, opencv_imgproc.MORPH_CLOSE, element5);
        OpenCVUtilsJava.show(closed, "Closed");

        // Opening
        Mat opened = new Mat();
        opencv_imgproc.morphologyEx(image, opened, opencv_imgproc.MORPH_OPEN, element5);
        OpenCVUtilsJava.show(opened, "Opened");
    }
    
}
