package opencv2_cookbook.chapter05;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.global.opencv_imgproc;

public class WatershedSegmenter {

    private Mat markers;

    public void setMarkers(Mat markerImage) {
        markers = new Mat();
        markerImage.convertTo(markers, opencv_core.CV_32SC1);
    }

    public Mat process(Mat image) {
        opencv_imgproc.watershed(image, markers);
        return markers;
    }

    public Mat segmentation() {
        // all segment with label higher than 255 will be assigned value 255
        Mat result = new Mat();
        markers.convertTo(result, opencv_core.CV_8U, 1 /* scale */, 0 /* shift */);
        return result;
    }

    public Mat watersheds() {
        Mat result = new Mat();
        markers.convertTo(result, opencv_core.CV_8U, 255 /* scale */, 255 /* shift */);
        return result;
    }

}
