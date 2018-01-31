package opencv2_cookbook.chapter07;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.indexer.IntRawIndexer;
import org.bytedeco.javacv.JavaCV;

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
    private Mat lines;
    
    /**
     * Apply probabilistic Hough transform.
     */
    public Mat findLines(Mat binary) {
        // Hough transform for line detection
        lines = new Mat();
        opencv_imgproc.HoughLinesP(binary, lines, deltaRho, deltaTheta, minVotes, minLength, minGap);
        return lines;
    }


    /**
     * Draws detected lines on an image
     */
    public void drawDetectedLines(Mat image) {

        IntRawIndexer indexer = lines.createIndexer();

        for (int i = 0; i < lines.rows(); i++) {
            Point pt1 = new Point(indexer.get(i, 0, 0), indexer.get(i, 0, 1));
            Point pt2 = new Point(indexer.get(i, 0, 2), indexer.get(i, 0, 3));

            // draw the segment on the image
            opencv_imgproc.line(image, pt1, pt2, new Scalar(0, 0, 255, 128), 1, opencv_core.LINE_AA, 0);
        }
    }
}
