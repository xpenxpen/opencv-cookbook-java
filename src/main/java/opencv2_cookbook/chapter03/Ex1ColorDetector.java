package opencv2_cookbook.chapter03;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

/**
 * Example for section "Using the Strategy pattern in algorithm design" in Chapter 3.
 * The pattern encapsulates an algorithm into a separate class,
 * in this case [[opencv2_cookbook.chapter03.ColorDetector]].
 *
 * The original example in the book is using "C++ API". Calls here use "C API" supported by JavaCV.
 */
public class Ex1ColorDetector {

    public static void main(String[] args) {

        // 1. Create image processor object
        ColorDetector colorDetector = new ColorDetector();
      
        // 2. Read input image
        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/boldt.jpg"), opencv_imgcodecs.IMREAD_COLOR);
      
        // 3. Set the input parameters
        colorDetector.setColorDistanceThreshold(100);
        // here blue sky
        colorDetector.setTargetColor(new ColorRGB(130, 190, 230));
      
        // 4. Process that input image and display the result
        Mat dest = colorDetector.process(src);

        // Display
        OpenCVUtilsJava.show(dest, "result");

    }
}
