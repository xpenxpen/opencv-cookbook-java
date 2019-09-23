package opencv2_cookbook.chapter07;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_imgproc.Vec3fVector;

/**
 * Detect circles using Hough transform approach.
 * The example for section "Detecting lines in image with the Hough transform" in Chapter 7, page 175.
 */
public class Ex4HoughCircles {

    public static void main(String[] args) {

        // Read input image
        Mat src = OpenCVUtilsJava.loadAndShowOrExit(new File("data/chariot.jpg"), opencv_imgcodecs.IMREAD_GRAYSCALE);

        // Blur with a Gaussian filter
        Mat smooth = new Mat();
        Size kernelSize = new Size(5, 5);
        double sigma = 1.5;
        int borderType = opencv_core.BORDER_DEFAULT;
        opencv_imgproc.GaussianBlur(src, smooth, kernelSize, sigma, sigma, borderType);
        OpenCVUtilsJava.show(smooth, "Blurred");

        // Compute Hough Circle transform
        // accumulator resolution (size of the image / 2)
        int dp = 2;
        // minimum distance between two circles
        int minDist = 33;
        // Canny high threshold
        int highThreshold = 200;
        // minimum number of votes
        int votes = 100;
        int minRadius = 40;
        int maxRadius = 90;
        Vec3fVector circles = new Vec3fVector();
        opencv_imgproc.HoughCircles(smooth, circles, opencv_imgproc.HOUGH_GRADIENT, dp, minDist, highThreshold, votes, minRadius,
                maxRadius);

        // Draw lines on the canny contour image
        Mat colorDst = new Mat();
        opencv_imgproc.cvtColor(src, colorDst, opencv_imgproc.COLOR_GRAY2BGR);
        for (int i = 0; i < circles.size(); i++) {
            Point center = new Point(opencv_core.cvRound(circles.get(i).get(0)), opencv_core.cvRound(circles.get(i).get(1)));
            int radius = opencv_core.cvRound(circles.get(i).get(2));
            System.out.println("Circle ((" + center.x() + "," + center.y() + "," + radius + ")");
            opencv_imgproc.circle(colorDst, center, radius, new Scalar(0, 0, 255, 0), 1, opencv_imgproc.LINE_AA, 0);
        }
        OpenCVUtilsJava.show(colorDst, "Hough Circles");
    }

}
