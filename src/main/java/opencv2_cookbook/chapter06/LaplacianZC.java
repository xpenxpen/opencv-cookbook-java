package opencv2_cookbook.chapter06;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.global.opencv_imgproc;

/**
 * Computation of Laplacian and zero-crossing.
 * Helper class for section "Computing the Laplacian of an image" in Chapter 6, page 156,
 * used in `Ex4Laplacian`.
 */
public class LaplacianZC {

    /**
     * Aperture size of the Laplacian kernel
     */
    public int aperture = 5;

    /**
     * Compute floating point Laplacian.
     */
    public Mat computeLaplacian(Mat src) {
        Mat laplace = new Mat();
        opencv_imgproc.Laplacian(src, laplace, opencv_core.CV_32F, aperture, 1 /* scale */, 0 /* delta */, opencv_core.BORDER_DEFAULT);
        return laplace;
    }

    /**
     * Get binary image of the zero-crossings
     * if the product of the two adjustment pixels is
     * less than threshold then this is a zero crossing
     * will be ignored.
     */
    public Mat getZeroCrossings(Mat laplace) {

        // Threshold at 0
        Mat signImage = new Mat();
        opencv_imgproc.threshold(laplace, signImage, 0, 255, opencv_imgproc.THRESH_BINARY);

        // Convert the +/- image into CV_8U
        Mat binary = new Mat();
        signImage.convertTo(binary, opencv_core.CV_8U);

        // Dilate the binary image +/- regions
        Mat dilated = new Mat();
        opencv_imgproc.dilate(binary, dilated, new Mat());

        // Return the zero-crossing contours
        Mat dest = new Mat();
        opencv_core.subtract(dilated, binary, dest);
        return dest;
    }

}
