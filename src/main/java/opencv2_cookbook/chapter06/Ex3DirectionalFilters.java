package opencv2_cookbook.chapter06;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * The example for section "Applying directional filters to detect edges" in Chapter 6, page 148.
 */
public class Ex3DirectionalFilters {

    public static void main(String[] args) {

        // Read input image
        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/boldt.jpg"), opencv_imgcodecs.IMREAD_GRAYSCALE);

        // Sobel edges in X
        Mat sobelX = new Mat();
        opencv_imgproc.Sobel(src, sobelX, opencv_core.CV_32F, 1, 0);
        OpenCVUtilsJava.show(OpenCVUtilsJava.toMat8U(sobelX), "Sobel X");

        // Sobel edges in Y
        Mat sobelY = new Mat();
        opencv_imgproc.Sobel(src, sobelY, opencv_core.CV_32F, 0, 1);
        OpenCVUtilsJava.show(OpenCVUtilsJava.toMat8U(sobelY), "Sobel Y");

        // Compute norm of directional images to create Sobel edge image
        Mat sobel = sobelX.clone();
        opencv_core.magnitude(sobelX, sobelY, sobel);
        OpenCVUtilsJava.show(OpenCVUtilsJava.toMat8U(sobel), "Sobel1");

        DoublePointer min = new DoublePointer(1);
        DoublePointer max = new DoublePointer(1);
        opencv_core.minMaxLoc(sobel, min, max, null, null, new Mat());
        System.out.println("Sobel min: " + min.get(0) + ", max: " + max.get(0) + ".");

        // Threshold edges
        // Prepare image for display: extract foreground
        Mat thresholded = new Mat();
        opencv_imgproc.threshold(sobel, thresholded, 100, 255, opencv_imgproc.THRESH_BINARY_INV);

        // FIXME: There us a crash if trying to display directly
        //   Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 16711426
        //       at java.awt.image.ComponentColorModel.getRGBComponent(ComponentColorModel.java:903)
        //  show(thresholded, "Thresholded")
        //  save(new File("Ex3DirectionalFilters-thresholded.tif"), thresholded)
        OpenCVUtilsJava.show(OpenCVUtilsJava.toMat8U(thresholded), "Thresholded");
    }

}
