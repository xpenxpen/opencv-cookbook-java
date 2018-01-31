package opencv2_cookbook.chapter04;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgcodecs;

/**
 * Example for section "Retrieving similar images using histogram comparison" in Chapter 4.
 */
public class Ex9ImageComparator {

    public static void main(String[] args) {
        File referenceImageFile = new File("data/waves.jpg");

        File[] testImageFiles = new File[]{
            new File("data/waves.jpg"),
            new File("data/beach.jpg"),
            new File("data/dog.jpg"),
            new File("data/polar.jpg"),
            new File("data/bear.jpg"),
            new File("data/lake.jpg"),
            new File("data/moose.jpg")
        };

        Mat reference = OpenCVUtilsJava.loadOrExit(referenceImageFile, opencv_imgcodecs.IMREAD_COLOR);
        
        ImageComparator comparator = new ImageComparator(reference, 8);
        
        // Show reference image after color reduction done by `ImageComparator`
        OpenCVUtilsJava.show(reference, "Reference");
        
        // Compute similarity for test images
        for (File file : testImageFiles) {
            Mat image = OpenCVUtilsJava.loadOrExit(file, opencv_imgcodecs.IMREAD_COLOR);
            int imageSize = image.cols() * image.rows();
            // Compute histogram match and normalize by image size.
            // 1 means perfect match.
            double score = comparator.compare(image) / imageSize;
            System.out.printf(file.getName() + ", score: %6.4f", score);
            OpenCVUtilsJava.show(image, file.getName() + ", score: " + score);
        }
    }

}
