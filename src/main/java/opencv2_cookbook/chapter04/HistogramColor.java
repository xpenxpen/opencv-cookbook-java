package opencv2_cookbook.chapter04;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * Helper class that simplifies usage of OpenCV `calcHist` function for color images.
 *
 * See OpenCV [[http://opencv.itseez.com/modules/imgproc/doc/histograms.html?highlight=histogram]]
 * documentation to learn backend details.
 */
public class HistogramColor {
    private int numberOfBins = 256;
    private IntPointer channels = new IntPointer(3);
    private Float _minRange = 0.0f;
    private Float _maxRange = 255.0f;

    public void setRanges(Float minRange, Float maxRange) {
        _minRange = minRange;
        _maxRange = maxRange;
    }
    
    public void setNumberOfBins(int numberOfBins) {
        this.numberOfBins = numberOfBins;
    }

    public Mat getHistogram(Mat image) {
        return getHistogram(image, new Mat());
    }

    //Sometimes not work
    //OpenCV Error: Assertion failed (j < nimages) in cv::histPrepareImages, file C:\projects\bytedeco\javacpp-presets\opencv\cppbuild\windows-x86_64\opencv-3.4.0\modules\imgproc\src\histogram.cpp, line 152
    //Exception in thread "main" java.lang.RuntimeException: C:\projects\bytedeco\javacpp-presets\opencv\cppbuild\windows-x86_64\opencv-3.4.0\modules\imgproc\src\histogram.cpp:152: error: (-215) j < nimages in function cv::histPrepareImages
    private Mat getHistogram(Mat image, Mat mask) {
        if (image == null) {
            throw new IllegalArgumentException();
        }
        if (image.channels() != 3) {
            throw new IllegalArgumentException("Expecting 3 channel (color) image");
        }
        
        Mat hist = new Mat();
        
        // Since C++ `calcHist` is using arrays of arrays we need wrap to do some wrapping
        // in `IntPointer` and `PointerPointer` objects.
        IntPointer intPtrChannels = new IntPointer(0, 1, 2);
        IntPointer intPtrHistSize = new IntPointer(numberOfBins, numberOfBins, numberOfBins);
        float[] histRange = new float[]{_minRange, _maxRange};
        FloatPointer floatPtrhistRange = new FloatPointer(histRange);
        PointerPointer<FloatPointer> ptrPtrHistRange = new PointerPointer<FloatPointer>(floatPtrhistRange, floatPtrhistRange, floatPtrhistRange);
        
        opencv_imgproc.calcHist(image,
                1, // histogram of 1 image only
                intPtrChannels, // the channel used
                mask, // no mask is used
                hist, // the resulting histogram
                3, // it is a 3D histogram
                intPtrHistSize, // number of bins
                ptrPtrHistRange, // pixel value range
                true, // uniform
                false); // no accumulation

        return hist;
    }

    /**
     * Convert input image from RGB ro HSV color space and compute histogram of the hue channel.
     */
    public Mat getHueHistogram(Mat image, int minSaturation) {
        if (image == null) {
            throw new IllegalArgumentException();
        }
        if (image.channels() != 3) {
            throw new IllegalArgumentException("Expecting 3 channel (color) image");
        }

        // Convert RGB to HSV color space
        Mat hsvImage = new Mat();
        opencv_imgproc.cvtColor(image, hsvImage, opencv_imgproc.COLOR_BGR2HSV);

        Mat saturationMask = new Mat();
        if (minSaturation > 0) {
            // Split the 3 channels into 3 images
            MatVector hsvChannels = new MatVector();
            opencv_core.split(hsvImage, hsvChannels);

            // Eliminate pixels with low saturation
            opencv_imgproc.threshold(hsvChannels.get(1), saturationMask, minSaturation, 255, opencv_imgproc.THRESH_BINARY);
        }

        // Prepare arguments for a 1D hue histogram
        Mat hist = new Mat();
        // range is from 0 to 180
        FloatBuffer histRanges = FloatBuffer.wrap(new float[] { 0f, 180f });
        // the hue channel
        IntBuffer channels = IntBuffer.wrap(new int[] { 0 });

        // Compute histogram
        opencv_imgproc.calcHist(hsvImage,
                1, // histogram of 1 image only
                channels, // the channel used
                saturationMask, // binary mask
                hist, // the resulting histogram
                1, // it is a 1D histogram
                OpenCVUtilsJava.wrapInIntBuffer(numberOfBins), // number of bins
                histRanges // pixel value range
                );

        return hist;
    }

}