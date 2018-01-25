package opencv2_cookbook.chapter04;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * Used by examples from section "Backprojecting a histogram to detect specific image content" in chapter 4.
 */
public class ContentFinder {
    private Mat histogram = new Mat();
    private float threshold = -1f;
    private boolean isSparse = false;
    
    /**
     * Find content back projecting a histogram.
     * @param image input used for back projection.
     * @return Result of the back-projection of the histogram. Image is binary (0,255) if threshold is larger than 0.
     */
    public Mat find(Mat image) {
        int[] channels = new int[] { 0, 1, 2 };
        return find(image, 0.0f, 255.0f, channels);
    }
    
    public Mat find(Mat image, float minValue, float maxValue, int[] channels) {
        Mat result = new Mat();

        // Create parameters that can be used for both 1D and 3D/color
        // histograms.
        // Since C++ calcBackProject is using arrays of arrays we need to do
        // some wrapping `PointerPointer` objects.
        float[] histRange = new float[] { minValue, maxValue };
        IntPointer intPtrChannels = new IntPointer(channels);
        FloatPointer floatPtrhistRange = new FloatPointer(histRange);
        PointerPointer<FloatPointer> ptrPtrHistRange = new PointerPointer<FloatPointer>(floatPtrhistRange, floatPtrhistRange, floatPtrhistRange);

        opencv_imgproc.calcBackProject(image, 1, intPtrChannels, histogram, result, ptrPtrHistRange, 255, true);

        if (threshold > 0) {
            opencv_imgproc.threshold(result, result, 255 * threshold, 255, opencv_imgproc.THRESH_BINARY);
        }

        return result;
    }
    
    public Mat getHistogram() {
        return histogram;
    }
    
    /**
     * Set reference histogram, it will be normalized.
     */
    public void setHistogram(Mat histogram) {
        isSparse = false;
        opencv_core.normalize(histogram, this.histogram);
    }
    
    public float getThreshold() {
        return threshold;
    }
    
    /**
     * Set threshold for converting the back-projected image to a binary.
     * If value is negative no thresholding will be done.
     */
    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }
    

}
