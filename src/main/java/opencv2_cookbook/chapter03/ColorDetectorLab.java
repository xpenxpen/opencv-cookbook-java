package opencv2_cookbook.chapter03;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.opencv.opencv_core.Mat;

public class ColorDetectorLab {
    private ColorLab targetColor = new ColorLab(74, -9, -26);
    private int colorDistanceThreshold;

    public Mat process(Mat image) {
        // Convert input from RGB to L*a*b* color space
        // Note that since destination image uses 8 bit unsigned integers, original L*a*b* values
        // are converted to fit 0-255 range
        //       L <- L*255/100
        //       a <- a + 128
        //       b <- b + 128
        Mat labImage = new Mat();
        opencv_imgproc.cvtColor(image, labImage, opencv_imgproc.COLOR_BGR2Lab);

        // Indexer for input image
        UByteRawIndexer srcIndexer = labImage.createIndexer();

        // Create output image and its indexer
        Mat dest = new Mat(labImage.rows(), labImage.cols(), opencv_core.CV_8U);
        UByteIndexer destIndexer = dest.createIndexer();

        // Iterate through pixels and check if their distance from the target
        // color is
        // within the distance threshold, if it is set `dest` to 255.
        for (int y = 0; y < labImage.rows(); y++) {
            for (int x = 0; x < labImage.cols(); x++) {
                byte t = 0;
                if (distance(colorAt(srcIndexer, y, x)) < colorDistanceThreshold) {
                    t = (byte) 255;
                }
                // Convert indexes to Long to avoid API ambiguity
                destIndexer.put(y, x, t);
            }
        }

        return dest;
    }

    private double distance(Triple color) {
        return Math.abs(targetColor.lAsUInt8 - color.l) / 255d * 100d
                + Math.abs(targetColor.aAsUInt8 - color.a)
                + Math.abs(targetColor.bAsUInt8 - color.b);
    }

    private Triple colorAt(UByteRawIndexer indexer, int c, int r) {
        return new Triple(indexer.get(c, r, 0), indexer.get(c, r, 1), indexer.get(c, r, 2));
    }
    
    public ColorLab getTargetColor() {
        return targetColor;
    }

    public void setTargetColor(ColorLab targetColor) {
        this.targetColor = targetColor;
    }

    public int getColorDistanceThreshold() {
        return colorDistanceThreshold;
    }

    public void setColorDistanceThreshold(int colorDistanceThreshold) {
        this.colorDistanceThreshold = colorDistanceThreshold;
    }

    class Triple {
        int l;
        int a;
        int b;
        public Triple(int l, int a, int b) {
            this.l = l;
            this.a = a;
            this.b = b;
        }
    }

}
