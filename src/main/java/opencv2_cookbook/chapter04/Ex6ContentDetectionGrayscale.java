package opencv2_cookbook.chapter04;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_imgcodecs;

/**
 * Uses histogram of a region in an gray scale image to create 'template',
 * looks through the whole image to detect pixels that are similar to that template.
 * Example for section "Backprojecting a histogram to detect specific image content" in Chapter 4.
 */
public class Ex6ContentDetectionGrayscale {

    public static void main(String[] args) {

        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/waves.jpg"), opencv_imgcodecs.IMREAD_GRAYSCALE);
        
        //Rect rectROI = new Rect(216, 33, 24, 30);
        Rect rectROI = new Rect(360, 55, 40, 50);
        
        // Display image with marked ROI
        OpenCVUtilsJava.show(OpenCVUtilsJava.drawOnImage(src, rectROI, new Scalar(1d, 255d, 255d, 0.5)), "Input");
        // Define ROI
        Mat imageROI = src.apply(rectROI);
        OpenCVUtilsJava.show(imageROI, "Reference");
        
        // Compute histogram within the ROI
        Histogram1D h = new Histogram1D();
        Mat hist = h.getHistogram(imageROI);
        OpenCVUtilsJava.show(h.getHistogramImage(imageROI), "Reference Histogram");

        ContentFinder finder = new ContentFinder();
        finder.setHistogram(hist);

        Mat result1 = finder.find(src);
        Mat tmp = new Mat();
        result1.convertTo(tmp, opencv_core.CV_8U, -1, 255);
        OpenCVUtilsJava.show(tmp, "Back-projection result");

        finder.setThreshold(0.12f);
        Mat result2 = finder.find(src);
        OpenCVUtilsJava.show(result2, "Detection result");
    }

}
