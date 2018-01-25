package opencv2_cookbook.chapter02;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.indexer.FloatIndexer;

public class Ex3Sharpen {

    public static void main(String[] args) {

        // Read input image
        Mat image = OpenCVUtilsJava.loadAndShowOrExit(new File("data/boldt.jpg"), opencv_imgcodecs.IMREAD_COLOR);
        
        // Define output image
        Mat dest = new Mat();

        // Construct sharpening kernel, oll unassigned values are 0
        Mat kernel = new Mat(3, 3, opencv_core.CV_32F, new Scalar(0));
        // Indexer is used to access value in the matrix
        FloatIndexer ki = kernel.createIndexer();
        ki.put(1, 1, 5);
        ki.put(0, 1, -1);
        ki.put(2, 1, -1);
        ki.put(1, 0, -1);
        ki.put(1, 2, -1);

        // Filter the image
        opencv_imgproc.filter2D(image, dest, image.depth(), kernel);

        // Display
        OpenCVUtilsJava.show(dest, "Sharpened");

    }

}
