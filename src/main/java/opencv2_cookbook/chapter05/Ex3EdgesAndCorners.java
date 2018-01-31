package opencv2_cookbook.chapter05;

import java.awt.image.BufferedImage;
import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * Example of detecting edges and corners using morphological filters. Based on section "Detecting edges and
 * corners using morphological filters".
 */
public class Ex3EdgesAndCorners {

    public static void main(String[] args) {

        // Read input image
        Mat image = OpenCVUtilsJava.loadAndShowOrExit(new File("data/building.jpg"), opencv_imgcodecs.IMREAD_GRAYSCALE);
        
        MorphoFeatures morpho = new MorphoFeatures();
        morpho.thresholdValue = 40;

        Mat edges = morpho.getEdges(image);
        OpenCVUtilsJava.show(edges, "Edges");

        morpho.thresholdValue = -1;
        Mat corners = morpho.getCorners(image);
        opencv_imgproc.morphologyEx(corners, corners, opencv_imgproc.MORPH_TOPHAT, new Mat());
        opencv_imgproc.threshold(corners, corners, 35, 255, opencv_imgproc.THRESH_BINARY_INV);
        BufferedImage cornersOnImage = morpho.drawOnImage(corners, image);
        OpenCVUtilsJava.show(cornersOnImage, "Corners on image");
    }
    
}
