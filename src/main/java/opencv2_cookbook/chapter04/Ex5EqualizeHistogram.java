package opencv2_cookbook.chapter04;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.global.opencv_imgcodecs;

/**
 * Modifies image using histogram equalization.
 * Example for section "Equalizing the image histogram" in Chapter 4.
 */
public class Ex5EqualizeHistogram {

    public static void main(String[] args) {

        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/group.jpg"), opencv_imgcodecs.IMREAD_GRAYSCALE);
        
        // Show histogram of the source image
        OpenCVUtilsJava.show(new Histogram1D().getHistogramImage(src), "Input histogram");

        // Apply look-up
        Mat dest = Histogram1D.equalize(src);
        
        // Show inverted image
        OpenCVUtilsJava.show(dest, "Equalized");
        // Show histogram of the modified image
        OpenCVUtilsJava.show(new Histogram1D().getHistogramImage(dest), "Equalized histogram");
    }

}
