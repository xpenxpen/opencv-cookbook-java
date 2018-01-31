package opencv2_cookbook.chapter07;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * The example for section "Extracting the components' contours" in Chapter 7, page 182.
 */
public class Ex5ExtractContours {

    public static void main(String[] args) {

        // Read input image
        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/binaryGroup.bmp"), opencv_imgcodecs.IMREAD_GRAYSCALE);

        // Extract connected components
        MatVector contours = new MatVector();
        Mat hierarchy = new Mat();
        opencv_imgproc.findContours(src, contours, opencv_imgproc.RETR_EXTERNAL, opencv_imgproc.CHAIN_APPROX_NONE, new Point(0, 0));

        Mat result = new Mat(src.size(), opencv_core.CV_8UC3, new Scalar(0));
        opencv_imgproc.drawContours(result, contours,
          -1, // draw all contours
          new Scalar(0, 0, 255, 0));
        OpenCVUtilsJava.show(result, "Contours");

        MatVector filteredContours = filterContours(contours);

        Mat result2 = OpenCVUtilsJava.loadOrExit(new File("data/group.jpg"), opencv_imgcodecs.IMREAD_COLOR);
        opencv_imgproc.drawContours(result2, filteredContours,
          -1, // draw all contours
          new Scalar(0, 0, 255, 0));
        OpenCVUtilsJava.show(result2, "Contours Filtered");
    }

    public static MatVector filterContours(MatVector matVector) {
        // Eliminate too short or too long contours
        int lengthMin = 100;
        int lengthMax = 1000;
        
        List<Mat> mats = new ArrayList<Mat>();
        for (int i = 0; i < matVector.size(); i++) {
            Mat mat = matVector.get(i);
            if (mat.total() > lengthMin && mat.total() < lengthMax) {
                mats.add(mat);
            }
        }
        
        return new MatVector(mats.toArray(new Mat[]{}));
    }

}
