package opencv2_cookbook.chapter08;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * The first example for section "Detecting Harris corners" in Chapter 8, page 192.
 *
 * Computes Harris corners strength image and displays after applying a corner strength threshold.
 * In the output image strong corners are marked with black, background with white.
 */
public class Ex1HarrisCornerMap {

    public static void main(String[] args) {

        // Read input image
        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/church01.jpg"), opencv_imgcodecs.IMREAD_GRAYSCALE);
        
        // Image to store the Harris detector responses.
        Mat cornerStrength = new Mat();
        // Detect Harris Corners
        opencv_imgproc.cornerHarris(src, cornerStrength,
          3 /* neighborhood size */ ,
          3 /* aperture size */ ,
          0.01 /* Harris parameter */);

        // Threshold to retain only locations of strongest corners
        Mat harrisCorners = new Mat();
        double t             = 0.0001;
        opencv_imgproc.threshold(cornerStrength, harrisCorners, t, 255, opencv_imgproc.THRESH_BINARY_INV);
        // FIXME: `show` should work without converting to 8U
        OpenCVUtilsJava.show(OpenCVUtilsJava.toMat8U(harrisCorners), "Harris Corner Map");

    }

}
