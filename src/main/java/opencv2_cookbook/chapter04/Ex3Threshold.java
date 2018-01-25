package opencv2_cookbook.chapter04;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * The third example for section "Computing the image histogram" in Chapter 4, page 93.
 * Separates pixels in an image into a foreground (black) and background (white) using OpenCV `threshold` method.
 */
public class Ex3Threshold {

    public static void main(String[] args) {

        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/group.jpg"), opencv_imgcodecs.IMREAD_GRAYSCALE);

        Mat dest = new Mat();
        opencv_imgproc.threshold(src, dest, 60, 255, opencv_imgproc.THRESH_BINARY);

        OpenCVUtilsJava.show(dest, "Thresholded");

    }

}
