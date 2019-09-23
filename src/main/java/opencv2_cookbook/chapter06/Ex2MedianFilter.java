package opencv2_cookbook.chapter06;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;

/**
 * The example for section "Filtering images using a median filter" in Chapter 6, page 147.
 */
public class Ex2MedianFilter {

    public static void main(String[] args) {

        // Read input image
        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/boldt_salt.jpg"), opencv_imgcodecs.IMREAD_GRAYSCALE);
        
        // Remove noise with a median filter
        Mat dest = new Mat();
        int kernelSize = 3;
        opencv_imgproc.medianBlur(src, dest, kernelSize);
        OpenCVUtilsJava.show(dest, "Median filtered");

        // Since median filter really cleans up outlier with values above (salt) and below (pepper),
        // in this case, we can reconstruct dark pixels that are most likely not effected by the noise.
        Mat dest2 = new Mat();
        opencv_core.min(src, dest, dest2);
        OpenCVUtilsJava.show(dest2, "Median filtered + dark pixel recovery");

    }

}
