package opencv2_cookbook.chapter05;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * Equivalent of C++ class MorphoFeatures presented in section "Detecting edges and filters using
 * morphological filters". Contains methods for morphological corner detection.
 */
public class MorphoFeatures {

    // Threshold to produce binary image
    public int thresholdValue = -1;

    // Structural elements used in corner detection
    private Mat cross = new Mat(5, 5, opencv_core.CV_8U,
      new BytePointer(new byte[]{
        0, 0, 1, 0, 0,
        0, 0, 1, 0, 0,
        1, 1, 1, 1, 1,
        0, 0, 1, 0, 0,
        0, 0, 1, 0, 0
      })
    );
    private Mat diamond = new Mat(5, 5, opencv_core.CV_8U,
      new BytePointer(new byte[]{
        0, 0, 1, 0, 0,
        0, 1, 1, 1, 0,
        1, 1, 1, 1, 1,
        0, 1, 1, 1, 0,
        0, 0, 1, 0, 0
      })
    );
    private Mat square  = new Mat(5, 5, opencv_core.CV_8U,
      new BytePointer(new byte[]{
        1, 1, 1, 1, 1,
        1, 1, 1, 1, 1,
        1, 1, 1, 1, 1,
        1, 1, 1, 1, 1,
        1, 1, 1, 1, 1
      })
    );
    private Mat x       = new Mat(5, 5, opencv_core.CV_8U,
      new BytePointer(new byte[]{
        1, 0, 0, 0, 1,
        0, 1, 0, 1, 0,
        0, 0, 1, 0, 0,
        0, 1, 0, 1, 0,
        1, 0, 0, 0, 1
      })
    );


    public Mat getEdges(Mat image) {
        // Get gradient image
        Mat result = new Mat();
        opencv_imgproc.morphologyEx(image, result, opencv_imgproc.MORPH_GRADIENT, new Mat());

        // Apply threshold to obtain a binary image
        applyThreshold(result);

        return result;
    }

    public Mat getCorners(Mat image) {
        Mat result = new Mat();
        // Dilate with a cross
        opencv_imgproc.dilate(image, result, cross);
        // Erode with a diamond
        opencv_imgproc.erode(result, result, diamond);

        Mat result2 = new Mat();
        // Dilate with X
        opencv_imgproc.dilate(image, result2, x);
        // Erode with a square
        opencv_imgproc.erode(result2, result2, square);

        // Corners are obtained by differentiating the two closed images
        opencv_core.absdiff(result2, result, result);

        // Apply threshold to get binary image
        applyThreshold(result);

        return result;
    }

    private void applyThreshold(Mat image) {
        if (thresholdValue > 0) {
            opencv_imgproc.threshold(image, image, thresholdValue, 255, opencv_imgproc.THRESH_BINARY_INV);
        }
    }

    /**
     * Draw circles at feature point locations on an image it assumes that images are of the same size.
     */
    public BufferedImage drawOnImage(Mat binary, Mat image) {

        // OpenCV drawing seems to crash a lot, so use Java2D
        Raster binaryRaster = OpenCVUtilsJava.toBufferedImage(binary).getData();
        int radius = 6;
        int diameter = radius * 2;

        BufferedImage imageBI = OpenCVUtilsJava.toBufferedImage(image);
        int width = imageBI.getWidth();
        int height = imageBI.getHeight();
        Graphics2D g2d = (Graphics2D) imageBI.getGraphics();
        g2d.setColor(Color.WHITE);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int v = binaryRaster.getSample(x, y, 0);
                if (v == 0) {
                    g2d.draw(new Ellipse2D.Double(x - radius, y - radius, diameter, diameter));
                }
            }
        }

        return imageBI;
    }

}
