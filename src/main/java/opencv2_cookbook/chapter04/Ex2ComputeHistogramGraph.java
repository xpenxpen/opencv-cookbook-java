package opencv2_cookbook.chapter04;

import java.awt.image.BufferedImage;
import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.global.opencv_imgcodecs;

/**
 * The second example for section "Computing the image histogram" in Chapter 4, page 92.
 * Displays a graph of a histogram created using utility class [[opencv2_cookbook.chapter04.Histogram1D]].
 */
public class Ex2ComputeHistogramGraph {

    public static void main(final String[] args) {
        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/group.jpg"), opencv_imgcodecs.IMREAD_GRAYSCALE);

        // Calculate histogram
        Histogram1D h = new Histogram1D();
        BufferedImage histogram = h.getHistogramImage(src);
        // Display the graph
        OpenCVUtilsJava.show(histogram, "Histogram");
    }
}