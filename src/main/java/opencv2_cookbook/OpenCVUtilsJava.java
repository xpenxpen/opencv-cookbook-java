package opencv2_cookbook;

import static org.bytedeco.opencv.global.opencv_core.CV_8U;
import static org.bytedeco.opencv.global.opencv_core.minMaxLoc;
import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_COLOR;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;

import javax.swing.JFrame;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Scalar;

/**
 * Helper methods that simplify use of OpenCV API.
 */
public class OpenCVUtilsJava {

    /** Load an image and show in a CanvasFrame. If image cannot be loaded the application will exit with code 1.
     *
     * @return loaded image
     */
    public Mat loadAndShowOrExit(File file){
        return loadAndShowOrExit(file,IMREAD_COLOR);
    }

    /** Load an image and show in a CanvasFrame. If image cannot be loaded the application will exit with code 1.
     *
     * @param flags Flags specifying the color type of a loaded image:
     *              <ul>
     *              <li> `>0` Return a 3-channel color image</li>
     *              <li> `=0` Return a gray scale image</li>
     *              <li> `<0` Return the loaded image as is. Note that in the current implementation
     *              the alpha channel, if any, is stripped from the output image. For example, a 4-channel
     *              RGBA image is loaded as RGB if the `flags` is greater than 0.</li>
     *              </ul>
     *              Default is gray scale.
     * @return loaded image
     */
    public static Mat loadAndShowOrExit(File file, Integer flags){
        Mat image = loadOrExit(file, flags);
        show(image,file.getName());
        return image;
    }


    /** Load an image. If image cannot be loaded the application will exit with code 1.
     *
     * @return loaded image
     */
    public static Mat loadOrExit(File file) {
        return loadOrExit(file,IMREAD_COLOR);
    }

    /** Load an image. If image cannot be loaded the application will exit with code 1.
     *
     * @param flags Flags specifying the color type of a loaded image:
     *              <ul>
     *              <li> `>0` Return a 3-channel color image</li>
     *              <li> `=0` Return a gray scale image</li>
     *              <li> `<0` Return the loaded image as is. Note that in the current implementation
     *              the alpha channel, if any, is stripped from the output image. For example, a 4-channel
     *              RGBA image is loaded as RGB if the `flags` is greater than 0.</li>
     *              </ul>
     *              Default is gray scale.
     * @return loaded image
     */
    public static Mat loadOrExit(File file, Integer flags) {
        Mat image = imread(file.getAbsolutePath(), flags);
        if(image.empty()){
            System.out.println("Couldn't load image: " + file.getAbsolutePath());
            System.exit(1);
        }
        return image;
    }

    /** Show image in a window. Closing the window will exit the application. */
    public static void show(Mat mat, String title) {
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        CanvasFrame canvas = new CanvasFrame(title, 1);
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.showImage(converter.convert(mat));
    }

    /** Show image in a window. Closing the window will exit the application. */
    public static void show(BufferedImage image, String title) {
        CanvasFrame canvas = new CanvasFrame(title, 1);
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.showImage(image);
    }
    
    /**
     * Draw a shape on an image.
     *
     * @param image input image
     * @param overlay shape to draw
     * @param color color to use
     * @return new image with drawn overlay
     */
    public static Mat drawOnImage(Mat image, Rect overlay, Scalar color) {
        Mat dest = image.clone();
        rectangle(dest, overlay, color);
        return dest;
    }
    
    /** Save the image to the specified file.
     *
     * The image format is chosen based on the filename extension (see `imread()` in OpenCV documentation for the list of extensions).
     * Only 8-bit (or 16-bit in case of PNG, JPEG 2000, and TIFF) single-channel or
     * 3-channel (with ‘BGR’ channel order) images can be saved using this function.
     * If the format, depth or channel order is different, use Mat::convertTo() , and cvtColor() to convert it before saving.
     *
     * @param file file to save to. File name extension decides output image format.
     * @param image image to save.
     */
    public void save(File file, Mat image) {
        imwrite(file.getAbsolutePath(), image);
    }
    
    public static BufferedImage toBufferedImage(Mat mat) {
        OpenCVFrameConverter.ToMat openCVConverter = new OpenCVFrameConverter.ToMat();
        Java2DFrameConverter java2DConverter = new Java2DFrameConverter();
        return java2DConverter.convert(openCVConverter.convert(mat));
    }
    
    /**
     * Creates a `MatVector` and put `mat` as its only element.
     */
    public static MatVector wrapInMatVector(Mat mat) {
        return new MatVector(mat);
    }
    
    /**
     * Creates a `IntBuffer` and put `v` as its only element.
     */
    public static IntBuffer wrapInIntBuffer(int v) {
        return IntBuffer.wrap(new int[]{v});
    }
    
    public static Mat toMat8U(Mat src) {
        return toMat8U(src, true);
    }

    /**
     * Convert `Mat` to one where pixels are represented as 8 bit unsigned integers (`CV_8U`).
     * It creates a copy of the input image.
     *
     * @param src input image.
     * @return copy of the input with pixels values represented as 8 bit unsigned integers.
     */
    public static Mat toMat8U(Mat src, boolean doScaling) {
        DoublePointer minVal = new DoublePointer(Double.MAX_VALUE);
        DoublePointer maxVal = new DoublePointer(Double.MIN_VALUE);
        minMaxLoc(src, minVal, maxVal, null, null, new Mat());
        double min = minVal.get(0);
        double max = maxVal.get(0);
        double scale = 1d;
        double offset = 0d;

        if (doScaling) {
            double s = 255d / (max - min);
            scale = s;
            offset = -min * s;
        }

        Mat dest = new Mat();
        src.convertTo(dest, CV_8U, scale, offset);
        return dest;
    }
    
}