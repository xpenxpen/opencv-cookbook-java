package opencv2_cookbook.chapter07;

import org.bytedeco.javacv.JavaCV;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_imgproc.Vec4iVector;

/**
 * Helper class to detect lines segments using probabilistic Hough transform approach.
 * The example for section "Detecting lines in image with the Hough transform" in Chapter 7, page 170.
 *
 * @see JavaCV sample [https://code.google.com/p/javacv/source/browse/javacv/samples/HoughLines.java]
 * @param deltaRho Accumulator resolution distance.
 * @param deltaTheta  Accumulator resolution angle.
 * @param minVotes Minimum number of votes that a line must receive before being considered.
 * @param minLength  Minimum number of votes that a line must receive before being considered.
 * @param minGap Max gap allowed along the line. Default no gap.
 */
public class LineFinder {
    public double deltaRho = 1;
    public double deltaTheta = Math.PI / 180;
    public int minVotes = 10;
    public double minLength = 0;
    public double minGap = 0d;
    private Vec4iVector lines;
    
    /**
     * Apply probabilistic Hough transform.
     */
    public Vec4iVector findLines(Mat binary) {
        // Hough transform for line detection
        lines = new Vec4iVector();
        opencv_imgproc.HoughLinesP(binary, lines, deltaRho, deltaTheta, minVotes, minLength, minGap);
        return lines;
    }


    /**
     * Draws detected lines on an image
     */
    public void drawDetectedLines(Mat image) {

        for (int i = 0; i < lines.size(); i++) {
            Point pt1 = new Point(lines.get(i).get(0), lines.get(i).get(1));
            Point pt2 = new Point(lines.get(i).get(2), lines.get(i).get(3));

            // draw the segment on the image
            opencv_imgproc.line(image, pt1, pt2, new Scalar(0, 0, 255, 128), 1, opencv_imgproc.LINE_AA, 0);
        }
    }
}
