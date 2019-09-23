package opencv2_cookbook.chapter04;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.javacpp.indexer.UByteIndexer;

/**
 * Creates inverted image by inverting its look-up table.
 * Example for section "Applying look-up table to modify image appearance" in Chapter 4.
 */
public class Ex4InvertLut {

    public static void main(String[] args) {

        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/group.jpg"), opencv_imgcodecs.IMREAD_GRAYSCALE);
        
        // Create inverted lookup table
        int dim = 256;
        Mat lut = new Mat(1, dim, opencv_core.CV_8U);
        UByteIndexer lutI = lut.createIndexer();
        for (int i = 0; i < dim; i++) {
            lutI.put(i, (byte)(dim - 1 - i));
        }

        // Apply look-up
        Mat dest = Histogram1D.applyLookUp(src, lut);

        OpenCVUtilsJava.show(dest, "Inverted LUT");

    }

}
