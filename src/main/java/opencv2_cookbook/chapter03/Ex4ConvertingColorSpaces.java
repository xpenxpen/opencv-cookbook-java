package opencv2_cookbook.chapter03;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_core.Mat;

/**
 * Example for section "Converting color spaces" in Chapter 3.
 * Colors are detected in L*a*b* color space rather than in RGB.
 *
 * Compare it to [[opencv2_cookbook.chapter03.Ex1ColorDetector]]
 */
public class Ex4ConvertingColorSpaces {

    public static void main(String[] args) {

        // 1. Create image processor object
        ColorDetectorLab colorDetector = new ColorDetectorLab();
      
        // 2. Read input image
        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/boldt.jpg"), opencv_imgcodecs.IMREAD_COLOR);
      
        // 3. Set the input parameters
        colorDetector.setColorDistanceThreshold(30);
        // here blue sky, RGB=(130, 190, 230) <=> L*a*b*=(74.3705, -9.0003, -25.9781)
        colorDetector.setTargetColor(new ColorLab(74.3705, -9.0003, -25.9781));
      
        // 4. Process that input image and display the result
        Mat dest = colorDetector.process(src);

        // Display
        OpenCVUtilsJava.show(dest, "result");

    }
}
