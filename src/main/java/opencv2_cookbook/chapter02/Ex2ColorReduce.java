package opencv2_cookbook.chapter02;

import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_COLOR;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.opencv.opencv_core.Mat;

public class Ex2ColorReduce {

    public static void main(String[] args) {

        // Read input image
        Mat image = OpenCVUtilsJava.loadAndShowOrExit(new File("data/boldt.jpg"), IMREAD_COLOR);

        Mat dest = colorReduce(image, 64);

        // Display
        OpenCVUtilsJava.show(dest, "Reduced colors");

    }

    /**
     * Reduce number of colors.
     *
     * @param image input image.
     * @param div color reduction factor
     */
    private static Mat colorReduce(Mat image, int div) {

        // Get access to image data
        UByteIndexer indexer = image.createIndexer();

        // Total number of elements, combining components from each channel
        int nbElements = image.rows() * image.cols() * image.channels();
        for (int i = 0; i < nbElements; i++) {
            // Convert to integer, byte is treated as an unsigned value
            int v = indexer.get(i) & 0xFF;
            // Use integer division to reduce number of values
            int newV = v / div * div + div / 2;
            // Put back into the image
            indexer.put(i, newV & 0xFF);
        }

        return image;
    }

}
