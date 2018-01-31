package opencv2_cookbook.chapter07;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.JavaCV;

/**
 * Detect lines segments using probabilistic Hough transform approach.
 * The example for section "Detecting lines in image with the Hough transform" in Chapter 7, page 170.
 *
 * @see JavaCV sample [https://code.google.com/p/javacv/source/browse/javacv/samples/HoughLines.java]
 */
public class Ex3HoughLineSegments {

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

        // Set probabilistic Hough transform
        LineFinder finder = new LineFinder();
        finder.minLength = 100;
        finder.minGap = 20;
        finder.minVotes = 80;

        finder.findLines(contours);

        // Draw lines on the canny contour image
        Mat colorDst = new Mat();
        opencv_imgproc.cvtColor(contours, colorDst, opencv_imgproc.COLOR_GRAY2BGR);
        finder.drawDetectedLines(colorDst);
        OpenCVUtilsJava.show(colorDst, "Hough Line Segments");
    }

}
