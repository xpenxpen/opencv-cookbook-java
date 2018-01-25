package opencv2_cookbook.chapter04;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.indexer.Indexer;

/**
 * Created by john on 16/08/16.
 */
public class Histogram1D {
    private int numberOfBins = 256;
    private IntPointer channels = new IntPointer(1);
    private Float _minRange = 0.0f;
    private Float _maxRange = 255.0f;

    public void setRanges(Float minRange, Float maxRange) {
        _minRange = minRange;
        _maxRange = maxRange;
    }

    public BufferedImage getHistogramImage(Mat image) {
        int width = this.numberOfBins;
        int height = this.numberOfBins;
        double[] hist = getHistogramAsArray(image);
        double scale = 0.9 / max(hist) * height;
        BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = canvas.createGraphics();

        // Paint background
        g.setPaint(Color.WHITE);
        g.fillRect(0, 0, width, height);


        // Draw a vertical line for each bin
        g.setPaint(Color.BLUE);

        for (int bin = 0; bin < numberOfBins; bin++) {
            int h = Long.valueOf(Math.round(hist[bin] * scale)).intValue();
            g.drawLine(bin, height - 1, bin, height - h - 1);
        }

        g.dispose();
        return canvas;
    }

    public double[] getHistogramAsArray(Mat image) {
        Mat hist = getHistogram(image);
        double[] dest = new double[numberOfBins];
        Indexer indexer = hist.createIndexer();
        for (int i = 0; i < numberOfBins; i++) {
            dest[i] = indexer.getDouble(i);
        }
        return dest;
    }
    
    /**
     * Apply a look-up table to an image. It is a wrapper for OpenCV function
     * `LUT`.
     */
    public static Mat applyLookUp(Mat image, Mat lookup) {
        Mat dest = new Mat();
        opencv_core.LUT(image, lookup, dest);
        return dest;
    }

    /**
     * Equalize histogram of an image. The algorithm normalizes the brightness
     * and increases the contrast of the image. It is a wrapper for OpenCV
     * function `equalizeHist`.
     */
    public static Mat equalize(Mat image) {
        Mat dest = new Mat();
        opencv_imgproc.equalizeHist(image, dest);
        return dest;
    }
    
    public Mat getHistogram(Mat image) {
        return getHistogram(image, new Mat());
    }

    //Sometimes not work
    //OpenCV Error: Assertion failed (j < nimages) in cv::histPrepareImages, file C:\projects\bytedeco\javacpp-presets\opencv\cppbuild\windows-x86_64\opencv-3.4.0\modules\imgproc\src\histogram.cpp, line 152
    //Exception in thread "main" java.lang.RuntimeException: C:\projects\bytedeco\javacpp-presets\opencv\cppbuild\windows-x86_64\opencv-3.4.0\modules\imgproc\src\histogram.cpp:152: error: (-215) j < nimages in function cv::histPrepareImages
    private Mat getHistogram(Mat image, Mat mask) {
        IntPointer histSize = new IntPointer(1);
        histSize.put(0, numberOfBins);
        FloatPointer ranges = new FloatPointer(_minRange, _maxRange);

        Mat hist = new Mat();
        opencv_imgproc.calcHist(image, 1, channels, mask, hist, 1, histSize, ranges);
        return hist;
    }

    //Sometimes not work
    private Mat getHistogram2(Mat image, Mat mask) {
        IntPointer histSize = new IntPointer(1);
        histSize.put(0, numberOfBins);
        FloatPointer ranges = new FloatPointer(_minRange, _maxRange);

        Mat hist = new Mat();
        opencv_imgproc.calcHist(OpenCVUtilsJava.wrapInMatVector(image), channels, mask, hist, histSize, ranges);
        return hist;
    }


    private double max(double[] dest) {
        double max = 0.0;
        for (double value : dest) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}