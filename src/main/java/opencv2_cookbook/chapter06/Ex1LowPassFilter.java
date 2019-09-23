package opencv2_cookbook.chapter06;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;

/**
 * The example for section "Filtering images using low-pass filters" in Chapter 6, page 142.
 * Basic use of a Gaussian filter.
 */
public class Ex1LowPassFilter {

    public static void main(String[] args) {

        // Read input image
        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/boldt.jpg"), opencv_imgcodecs.IMREAD_GRAYSCALE);
        
        // Blur with a Gaussian filter
        //    val dest = cvCreateImage(cvGetSize(src), src.depth, 1)
        Mat dest = new Mat();
        Size kernelSize = new Size(5, 5);
        double sigma = 1.5;
        int borderType = opencv_core.BORDER_DEFAULT;
        opencv_imgproc.blur(src, dest, kernelSize);
        OpenCVUtilsJava.show(dest, "Blurred");
        
        Mat dest2 = new Mat();
        opencv_imgproc.GaussianBlur(src, dest2, kernelSize, sigma);
        OpenCVUtilsJava.show(dest2, "GaussianBlur");

    }

}
