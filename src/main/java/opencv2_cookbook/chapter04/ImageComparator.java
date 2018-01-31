package opencv2_cookbook.chapter04;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * Computes image similarity using `compareHist`.
 */
public class ImageComparator {
    private HistogramColor hist;
    private Mat referenceHistogram;
    
    public ImageComparator(Mat referenceImage, int numberOfBins) {
        hist = new HistogramColor();
        hist.setNumberOfBins(numberOfBins);
        referenceHistogram = hist.getHistogram(referenceImage);
    }


    /**
     * Compare the reference image with the given input image and return similarity score.
     */
    public double compare(Mat image) {
        Mat inputH = hist.getHistogram(image);
        return opencv_imgproc.compareHist(referenceHistogram, inputH, opencv_imgproc.HISTCMP_INTERSECT);
    }

}
