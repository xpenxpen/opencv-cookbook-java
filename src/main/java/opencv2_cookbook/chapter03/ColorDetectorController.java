package opencv2_cookbook.chapter03;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgcodecs;

/**
 * Implements the controller for the color corrector example in Chapter 3
 * sections "Using a controller to communicate with processing modules"
 * and "Using the Singleton design pattern".
 */
public class ColorDetectorController {

    private ColorDetector colorDetector = new ColorDetector();

    /**
     * Image to be processed.
     */
    public Mat inputImage;

    /**
     * Image result.
     */
    public Mat result;


    /**
     * Get the color distance threshold.
     */
    public int getColorDistanceThreshold() {
        return colorDetector.getColorDistanceThreshold();
    }


    /**
     * Set the color distance threshold.
     */
    public void setColorDistanceThreshold(int colorDistanceThreshold) {
        colorDetector.setColorDistanceThreshold(colorDistanceThreshold);
    }

    /**
     * Get the color to be detected
     */
    public ColorRGB getTargetColor() {
        return colorDetector.getTargetColor();
    }

    /**
     * Set the color to be detected
     */
    public void setTargetColor(ColorRGB targetColor) {
        colorDetector.setTargetColor(targetColor);
    }

    /**
     * Read the input image from a file. Return `true` if reading completed successfully.
     */
    public boolean setInputImage(String fileName) {
        inputImage = imread(fileName, opencv_imgcodecs.IMREAD_UNCHANGED);
        return !inputImage.empty();
    }


    /**
     * Perform image processing.
     */
    public void process() {
        result = colorDetector.process(inputImage);
    }

}
