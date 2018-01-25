package opencv2_cookbook.chapter03;

import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;

/**
 * Example of using a strategy pattern in algorithm design.
 *
 * The pattern encapsulates an algorithm into a separate class. To run this
 * example use [[opencv2_cookbook.chapter03.Ex1ColorDetector]].
 *
 * The algorithm converts the input image to a binary by checking is pixel color
 * is within a given distance from a desired color. Pixels with color close to
 * the desired color are white, other black.
 *
 * This code functionally is equivalent to C++ code in chapter 3 section
 * "Using the Strategy pattern in algorithm design". The original example in the
 * book is using "C++ API". Here we use JavaCPP Indexer to access pixel values
 * in the image.
 *
 * Unlike the in the C++ example, this class does not pre-allocates and hold
 * space for process image, it is create only when needed.
 */
public class ColorDetector {
    private ColorRGB targetColor = new ColorRGB(130, 190, 230);
    private int colorDistanceThreshold;

    public Mat process(Mat image) {

        // Indexer for input image
        UByteIndexer srcI = image.createIndexer();

        // Create output image and its indexer
        Mat dest = new Mat(image.rows(), image.cols(), opencv_core.CV_8U);
        UByteIndexer destI = dest.createIndexer();

        // Iterate through pixels and check if their distance from the target
        // color is
        // within the distance threshold, if it is set `dest` to 255.
        int[] brg = new int[3];
        for (int y = 0; y < image.rows(); y++) {
            for (int x = 0; x < image.cols(); x++) {
                srcI.get(y, x, brg);
                ColorRGB c = ColorRGB.fromBGR(brg);
                byte t = 0;
                if (distance(c) < colorDistanceThreshold) {
                    t = (byte) 255;
                }
                // Convert indexes to Long to avoid API ambiguity
                destI.put(y, x, t);
            }
        }

        return dest;
    }

    private double distance(ColorRGB color) {
        return Math.abs(targetColor.red() - color.red())
                + Math.abs(targetColor.green() - color.green())
                + Math.abs(targetColor.blue() - color.blue());
    }

    public ColorRGB getTargetColor() {
        return targetColor;
    }

    public void setTargetColor(ColorRGB targetColor) {
        this.targetColor = targetColor;
    }

    public int getColorDistanceThreshold() {
        return colorDistanceThreshold;
    }

    public void setColorDistanceThreshold(int colorDistanceThreshold) {
        this.colorDistanceThreshold = colorDistanceThreshold;
    }
}
