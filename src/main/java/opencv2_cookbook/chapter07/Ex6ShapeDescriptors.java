package opencv2_cookbook.chapter07;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Moments;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Point2f;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * The example for section "Computing components' shape descriptors" in Chapter 7, page 186.
 */
public class Ex6ShapeDescriptors {

    public static void main(String[] args) {

        Scalar Red     = new Scalar(0, 0, 255, 0);
        Scalar Magenta = new Scalar(255, 0, 255, 0);
        Scalar Yellow  = new Scalar(0, 255, 255, 0);
        Scalar Blue    = new Scalar(255, 0, 0, 0);
        Scalar Cyan    = new Scalar(255, 255, 0, 0);
        Scalar Green   = new Scalar(0, 255, 0, 0);

        //
        // First part is the same as in example `Ex5ExtractContours`; extracts contours.
        //

        // Read input image
        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/binaryGroup.bmp"), opencv_imgcodecs.IMREAD_GRAYSCALE);

        // Extract connected components
        MatVector contourVec = new MatVector();
        CvMemStorage storage    = opencv_core.cvCreateMemStorage();
        opencv_imgproc.findContours(src, contourVec, opencv_imgproc.RETR_EXTERNAL, opencv_imgproc.CHAIN_APPROX_NONE);

        // Draw extracted contours
        Mat colorDst = new Mat(src.size(), opencv_core.CV_8UC3, new Scalar(0));
        opencv_imgproc.drawContours(colorDst, contourVec, -1 /* draw all contours */ , Red);
        OpenCVUtilsJava.show(colorDst, "Contours");

        MatVector filteredContours = Ex5ExtractContours.filterContours(contourVec);

        Mat colorDest2 = OpenCVUtilsJava.loadOrExit(new File("data/group.jpg"), opencv_imgcodecs.IMREAD_COLOR);
        opencv_imgproc.drawContours(colorDest2, filteredContours, -1, Red, 2, opencv_core.LINE_8, opencv_core.noArray(), Integer.MAX_VALUE, new Point());

        //
        // Second part computes shapes descriptors from the extracted contours.
        //

        // Testing the bounding box
        //  val update = 0
        Rect rectangle0 = opencv_imgproc.boundingRect(filteredContours.get(0));
        // Draw rectangle
        opencv_imgproc.rectangle(colorDest2, rectangle0, Magenta, 2, opencv_core.LINE_AA, 0);

        // Testing the enclosing circle
        Point2f center1 = new Point2f();
        FloatPointer radius1 = new FloatPointer(1f);
        opencv_imgproc.minEnclosingCircle(filteredContours.get(1), center1, radius1);
        // Draw circle
        opencv_imgproc.circle(colorDest2, new Point(opencv_core.cvRound(center1.x()), opencv_core.cvRound(center1.y())), (int)radius1.get(0), Yellow, 2, opencv_core.LINE_AA, 0);

        // Testing the approximate polygon
        Mat poly2 = new Mat();
        opencv_imgproc.approxPolyDP(filteredContours.get(2), poly2, 5, true);
        // Draw only the first poly.
        opencv_imgproc.polylines(colorDest2, new MatVector(poly2), true, Blue, 2, opencv_core.LINE_AA, 0);

        // Testing the convex hull
        boolean clockwise    = true;
        boolean returnPoints = true;
        Mat hull         = new Mat();
        opencv_imgproc.convexHull(filteredContours.get(3), hull, clockwise, returnPoints);
        opencv_imgproc.polylines(colorDest2, new MatVector(hull), true, Cyan, 2, opencv_core.LINE_AA, 0);

        // Testing the moments for all filtered contours, and marking center of mass on the image
        for (int i = 0; i < filteredContours.size(); i++) {
          Moments ms = opencv_imgproc.moments(filteredContours.get(i));
          int xCenter = (int)(Math.round(ms.m10() / ms.m00()));
          int yCenter = (int)(Math.round(ms.m01() / ms.m00()));
          opencv_imgproc.circle(colorDest2, new Point(xCenter, yCenter), 2, Green, 2, opencv_core.LINE_AA, 0);
        }

        // Show the image with marked contours and shape descriptors
        OpenCVUtilsJava.show(colorDest2, "Some Shape Descriptors");
    }

}
