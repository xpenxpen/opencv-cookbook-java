package opencv2_cookbook.chapter05;

import java.io.File;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 * Example from section "Segmenting images using watersheds".
 */
public class Ex4WatershedSegmentation {

    public static void main(String[] args) {

        // Read input image
        Mat image = OpenCVUtilsJava.loadAndShowOrExit(new File("data/group.jpg"), opencv_imgcodecs.IMREAD_COLOR);
        Mat binary = OpenCVUtilsJava.loadAndShowOrExit(new File("data/binary.bmp"), opencv_imgcodecs.IMREAD_GRAYSCALE);

        // Eliminate noise and smaller objects, repeat erosion 6 times
        Mat fg = new Mat();
        opencv_imgproc.erode(binary, fg, new Mat() /* 3x3 square */, new Point(-1, -1), 6 /* iterations */,
                opencv_core.BORDER_CONSTANT, opencv_imgproc.morphologyDefaultBorderValue());
        OpenCVUtilsJava.show(fg, "Foreground");

        // Identify image pixels pixels objects
        Mat bg = new Mat();
        opencv_imgproc.dilate(binary, bg, new Mat() /* 3x3 square */, new Point(-1, -1), 6 /* iterations */,
                opencv_core.BORDER_CONSTANT, opencv_imgproc.morphologyDefaultBorderValue());
        OpenCVUtilsJava.show(bg, "Dilated");

        opencv_imgproc.threshold(bg, bg, 1 /* threshold */, 128 /* max value */, opencv_imgproc.THRESH_BINARY_INV);
        OpenCVUtilsJava.show(bg, "Background");

        // Create marker image
        Mat markers = new Mat(binary.size(), opencv_core.CV_8U, new Scalar(0d));
        opencv_core.add(fg, bg, markers);
        OpenCVUtilsJava.show(markers, "Markers");

        WatershedSegmenter segmenter = new WatershedSegmenter();
        segmenter.setMarkers(markers);

        Mat segmentMarkers = segmenter.process(image);
        OpenCVUtilsJava.show(segmentMarkers, "segmentMarkers");

        Mat segmentation = segmenter.segmentation();
        OpenCVUtilsJava.show(segmentation, "Segmentation");

        Mat watershed = segmenter.watersheds();
        OpenCVUtilsJava.show(watershed, "Watersheds");
    }

}
