package opencv2_cookbook.chapter05;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * Example of using morphological erosion and dilation in chapter 5 section
 * "Eroding and dilating images using morphological filters".
 */
public class Ex1ErodingAndDilating {

    public static void main(String[] args) {

        // Read input image
        Mat image = OpenCVUtilsJava.loadAndShowOrExit(new File("data/binary.bmp"), opencv_imgcodecs.IMREAD_COLOR);
        
        // Erode the image, by default 3x3 element is used
        Mat eroded = new Mat();
        opencv_imgproc.erode(image, eroded, new Mat());
        OpenCVUtilsJava.show(eroded, "Eroded");

        // Dilate image, by default 3x3 element is used
        Mat dilated = new Mat();
        opencv_imgproc.dilate(image, dilated, new Mat());
        OpenCVUtilsJava.show(dilated, "Dilated");

        // Erode with 7x7 structural element
        // First define rectangular kernel of size 7x7.
        Mat eroded7x7 = new Mat();
        // Note that scalar argument is Double meaning that is is an initial value, value of Int would mean size.
        Mat element = new Mat(7, 7, opencv_core.CV_8U, new Scalar(1d));
        opencv_imgproc.erode(image, eroded7x7, element);
        OpenCVUtilsJava.show(eroded7x7, "Eroded 7x7");

        // Erode with 7x7 structural element
        // You can do it using 3x3 kernel and iterating 3 times.
        // Note: iterating 2 times will give 5x5 kernel equivalent, iterating 4 times will get 9x9, ...
        Mat eroded3x3i3 = new Mat();
        opencv_imgproc.erode(image, eroded3x3i3, new Mat(), new Point(-1, -1), 3, opencv_core.BORDER_CONSTANT, opencv_imgproc.morphologyDefaultBorderValue());
        OpenCVUtilsJava.show(eroded3x3i3, "Eroded 3x3, 3 times (effectively 7x7)");
    }
    
}
