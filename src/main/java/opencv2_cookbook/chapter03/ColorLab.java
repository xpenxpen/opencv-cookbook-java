package opencv2_cookbook.chapter03;


/**
 * Represents a color in L*a*b* color space. Color values are expected to be in usual L*a*b* range:
 * L* 0 to 100,
 * a* -100 to 100,
 * b* -100 to 100.
 *
 * The color component values can be also read as UInt8 numbers the way OpenCV encodes UInt8 L*a*b* values:
 * L <- L*255/100,
 * a <- a + 128,
 * b <- b + 128.
 * See [[http://opencv.itseez.com/modules/imgproc/doc/miscellaneous_transformations.html?highlight=cvtcolor#void%20cvCvtColor(const%20CvArr*%20src,%20CvArr*%20dst,%20int%20code)]] documentation.
 */
public class ColorLab {
    public int lAsUInt8;
    public int aAsUInt8;
    public int bAsUInt8;
    
    /**
     * Represents a color in RGB color space. Component values are expected to be in range [0-255]
     */
    public ColorLab(double l, double a, double b) {
        this.lAsUInt8 = Double.valueOf(l * 255 / 100).intValue();
        this.aAsUInt8 = Double.valueOf(a + 128).intValue();
        this.bAsUInt8 = Double.valueOf(b + 128).intValue();
    }
}
