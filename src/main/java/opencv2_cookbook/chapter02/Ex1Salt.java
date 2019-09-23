package opencv2_cookbook.chapter02;

import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_COLOR;

import java.io.File;
import java.util.Random;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.opencv.opencv_core.Mat;

public class Ex1Salt {

    public static void main(String[] args) {

        // Read input image
        Mat image = OpenCVUtilsJava.loadAndShowOrExit(new File("data/boldt.jpg"), IMREAD_COLOR);

        // Add salt noise
        Mat dest = salt(image, 2000);

        // Display
        OpenCVUtilsJava.show(dest, "Salted");

    }

    /**
     * Add 'salt' noise.
     *
     * @param image input image.
     * @param n number of 'salt' grains.
     */
    private static Mat salt(Mat image, int n) {

        // Random number generator
        Random random = new Random();

        // Get access to image data
        UByteIndexer indexer = image.createIndexer();

        // Place `n` grains at random locations
        int nbChannels = image.channels();
        for (int i = 0; i < n; i++) {
            // Create random index of a pixel
            int row = random.nextInt(image.rows());
            int col = random.nextInt(image.cols());
            // Set it to white by setting each of the channels to max (255)
            for (int j = 0; j < nbChannels; j++) {
                indexer.put(row, col, j, 255);
            }
        }

        return image;
    }

}
