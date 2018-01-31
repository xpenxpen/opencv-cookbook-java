package opencv2_cookbook.chapter05;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * Example from section "Extracting foreground objects with the GrabCut algorithm".
 */
public class Ex5GrabCut {

    public static void main(String[] args) {

        // Read input image
        Mat image = OpenCVUtilsJava.loadAndShowOrExit(new File("data/group.jpg"), opencv_imgcodecs.IMREAD_COLOR);

        // Define bounding rectangle, pixels outside this rectangle will be labeled as background.
        Rect rectangle = new Rect(10, 100, 380, 180);

        Mat result = new Mat();
        int iterCount = 5;
        int mode = opencv_imgproc.GC_INIT_WITH_RECT;

        // Need to allocate arrays for temporary data
        Mat bgdModel = new Mat();
        Mat fgdModel = new Mat();

        // GrabCut segmentation
        opencv_imgproc.grabCut(image, result, rectangle, bgdModel, fgdModel, iterCount, mode);

        // Prepare image for display: extract foreground
        opencv_imgproc.threshold(result, result, opencv_imgproc.GC_PR_FGD - 0.5, opencv_imgproc.GC_PR_FGD + 0.5,
                opencv_imgproc.THRESH_BINARY);
        OpenCVUtilsJava.show(OpenCVUtilsJava.toMat8U(result), "Result foreground mask");
//        
//        Mat foreground = new Mat(image.size(), opencv_core.CV_8UC3, new Scalar(255d));
//        //result= result&1;
//        foreground.create(image.size(), opencv_core.CV_8UC3);
//        foreground.setTo(new Scalar(255d));
//        image.copyTo(foreground,result); // bg pixels not copied
    }
    
}
