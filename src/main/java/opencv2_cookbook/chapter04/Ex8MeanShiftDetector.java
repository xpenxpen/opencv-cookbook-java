package opencv2_cookbook.chapter04;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.TermCriteria;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_video;

/**
 * Uses the mean shift algorithm to find best matching location of the 'template' in another image.
 *
 * Matching is done using the hue channel of the input image converted to HSV color space.
 * Histogram of a region in the hue channel is used to create a 'template'.
 *
 * The target image, where we want to find a matching region, is also converted to HSV. Histogram of the template is back
 * projected in the hue channel.
 * The mean shift algorithm searches in the back projected image to find best match to the template.
 *
 * Example for section "Using the mean shift algorithm to find an object" in Chapter 4.
 */
public class Ex8MeanShiftDetector {

    public static void main(String[] args) {
        
        Scalar red = new Scalar(0, 0, 255, 128);

        Mat templateImage = OpenCVUtilsJava.loadAndShowOrExit(new File("data/baboon1.jpg"), opencv_imgcodecs.IMREAD_COLOR);
        
        Rect rect = new Rect(110, 260, 35, 40);
        
        // Display image with marked ROI
        OpenCVUtilsJava.show(OpenCVUtilsJava.drawOnImage(templateImage, rect, red), "Input");
        // Define ROI
        Mat imageROI = templateImage.apply(rect);
        
        // Compute histogram within the ROI
        int minSaturation = 65;
        HistogramColor h = new HistogramColor();
        //h.setNumberOfBins(8);
        Mat templateHueHist = h.getHueHistogram(imageROI, minSaturation);

        ContentFinder finder = new ContentFinder();
        finder.setHistogram(templateHueHist);
        finder.setThreshold(0.2f);
        
        //
        //  Search a target image for best match to the 'template'
        //

        // Load the second image where we want to locate a new baboon face
        Mat targetImage = OpenCVUtilsJava.loadAndShowOrExit(new File("data/baboon3.jpg"), opencv_imgcodecs.IMREAD_COLOR);

        // Convert to HSV color space
        Mat hsvTargetImage = new Mat();
        opencv_imgproc.cvtColor(targetImage, hsvTargetImage, opencv_imgproc.COLOR_BGR2HSV);

        // Get back-projection of hue histogram
        Mat hueBackProjectionImage = finder.find(hsvTargetImage, 0f, 180f, new int[]{0});
        OpenCVUtilsJava.show(hueBackProjectionImage, "Backprojection of second image");

        // Search for object with mean-shift
        TermCriteria criteria = new TermCriteria(TermCriteria.MAX_ITER, 10, 0.01);
        int r = opencv_video.meanShift(hueBackProjectionImage, rect, criteria);
        System.out.println("meanshift = " + r);

        OpenCVUtilsJava.show(OpenCVUtilsJava.drawOnImage(targetImage, rect, red), "Image 2 result in " + r + " iterations");
    }

}
