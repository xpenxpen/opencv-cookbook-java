package opencv2_cookbook.chapter04;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_imgcodecs;

/**
 * Uses histogram of region in an color image to create 'template'.
 * looks through the whole image to detect pixels that are  similar to that template.
 * Example for section "Backprojecting a histogram to detect specific image content" in Chapter 4.
 */
public class Ex7ContentDetectionColor {

    public static void main(String[] args) {

        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/waves.jpg"), opencv_imgcodecs.IMREAD_COLOR);
        
        //Rect rectROI = new Rect(0, 0, 165, 75);
        Rect rectROI = new Rect(360, 55, 40, 50);
        
        // Display image with marked ROI
        OpenCVUtilsJava.show(OpenCVUtilsJava.drawOnImage(src, rectROI, new Scalar(0d, 255d, 255d, 0.5)), "Input");
        // Define ROI
        Mat imageROI = src.apply(rectROI);
        OpenCVUtilsJava.show(imageROI, "Reference");
        
        // Compute histogram within the ROI
        HistogramColor h = new HistogramColor();
        h.setNumberOfBins(8);
        Mat hist = h.getHistogram(imageROI);
        //OpenCVUtilsJava.show(h.getHistogramImage(imageROI), "Reference Histogram");

        ContentFinder finder = new ContentFinder();
        finder.setHistogram(hist);
        finder.setThreshold(0.05f);

        Mat result1 = finder.find(src);
        OpenCVUtilsJava.show(result1, "Color Detection Result");
        
        // Second color image
        Mat colorImage2 = OpenCVUtilsJava.loadAndShowOrExit(new File("data/dog.jpg"), opencv_imgcodecs.IMREAD_COLOR);

        Mat result2 = finder.find(colorImage2);
        OpenCVUtilsJava.show(result2, "Color Detection Result 2");
    }

}
