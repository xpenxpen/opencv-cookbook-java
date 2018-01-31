package opencv2_cookbook.chapter07;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.indexer.FloatRawIndexer;
import org.bytedeco.javacv.JavaCV;

/**
 * Detect lines using standard Hough transform approach.
 * The example for section "Detecting lines in image with the Hough transform" in Chapter 7, page 167.
 *
 * @see JavaCV sample [https://code.google.com/p/javacv/source/browse/javacv/samples/HoughLines.java]
 */
public class Ex2HoughLines {

    public static void main(String[] args) {

        // Read input image
        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/road.jpg"), opencv_imgcodecs.IMREAD_GRAYSCALE);
        
        // Canny contours
        Mat canny = new Mat();
        int threshold1 = 125;
        int threshold2 = 350;
        int apertureSize = 3;
        opencv_imgproc.Canny(src, canny, threshold1, threshold2, apertureSize, true /*L2 gradient*/);
        
        OpenCVUtilsJava.show(canny, "Canny Contours");

        // Hough transform for line detection
        Mat lines = new Mat();
        CvMemStorage storage = opencv_core.cvCreateMemStorage(0);
        int method = opencv_imgproc.HOUGH_STANDARD;
        int distanceResolutionInPixels = 1;
        double angleResolutionInRadians = Math.PI / 180;
        int minimumVotes = 80;
        opencv_imgproc.HoughLines(
          canny,
          lines,
          distanceResolutionInPixels,
          angleResolutionInRadians,
          minimumVotes);

        // Draw lines on the canny contour image
        FloatRawIndexer indexer = lines.createIndexer();
        Mat result  = new Mat();
        src.copyTo(result);
        opencv_imgproc.cvtColor(src, result, opencv_imgproc.COLOR_GRAY2BGR);
        
        for (int i = 0; i < lines.rows(); i++) {
            float rho = indexer.get(i, 0, 0);
            float theta = indexer.get(i, 0, 1);
            Point p1;
            Point p2;

            if (theta < Math.PI / 4.0 || theta > 3.0 * Math.PI / 4.0) {
                // ~vertical line
                // point of intersection of the line with first row
                p1 = new Point((int) Math.round(rho / Math.cos(theta)), 0);
                // point of intersection of the line with last row
                p2 = new Point((int) Math.round((rho - result.rows() * Math.sin(theta)) / Math.cos(theta)), result.rows());
            } else {
                // ~horizontal line
                // point of intersection of the line with first column
                p1 = new Point(0, (int) Math.round(rho / Math.sin(theta)));
                // point of intersection of the line with last column
                p2 = new Point(result.cols(), (int) Math.round((rho - result.cols() * Math.cos(theta)) / Math.sin(theta)));
            }

            // draw a white line
            opencv_imgproc.line(result, p1, p2, new Scalar(0, 0, 255, 0), 1, opencv_core.LINE_8, 0);
        }

        //save(new File("result.tif"), result);
        OpenCVUtilsJava.show(OpenCVUtilsJava.toMat8U(result), "Hough Lines");
    }

}
