package opencv2_cookbook.chapter03;

import java.awt.Color;

public class ColorRGB {
    private Color color;
    
    /**
     * Represents a color in RGB color space. Component values are expected to be in range [0-255]
     */
    public ColorRGB(int r, int g, int b) {
        this.color = new Color(r, g, b);
    }
    
    public ColorRGB(Color color) {
        this.color = color;
    }
    
    public Color getColor() {
        return color;
    }
    
    public int red() {
        return color.getRed();
    }
    
    public int green() {
        return color.getGreen();
    }
    
    public int blue() {
        return color.getBlue();
    }

    /** Factory method for creating a color from a 3 byte array. */
    public static ColorRGB fromBGR(byte[] b) {
        assert b.length == 3;
        return new ColorRGB(b[2] & 0xFF, b[1] & 0xFF, b[0] & 0xFF);
    }

    /** Factory method for creating a color from a 3 int array. */
    public static ColorRGB fromBGR(int[] b) {
        assert b.length == 3;
        return new ColorRGB(b[2], b[1], b[0]);
    }
}
