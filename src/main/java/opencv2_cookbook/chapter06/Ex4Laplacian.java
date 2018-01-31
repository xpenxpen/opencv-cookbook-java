package opencv2_cookbook.chapter06;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgcodecs;

/**
 * The example for section "Computing the Laplacian of an image" in Chapter 6, page 156.
 */
public class Ex4Laplacian {

    public static void main(String[] args) {

        // Read input image
        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/boldt.jpg"), opencv_imgcodecs.IMREAD_GRAYSCALE);

        // Compute floating point Laplacian edge strength
        LaplacianZC laplacian = new LaplacianZC();
        laplacian.aperture = 7;
        Mat flap = laplacian.computeLaplacian(src);
        OpenCVUtilsJava.show(OpenCVUtilsJava.toMat8U(flap), "Laplacian");

        // Locate edges using zero-crossing
        Mat edges = laplacian.getZeroCrossings(flap);
        OpenCVUtilsJava.show(edges, "Laplacian Edges");
    }

}
