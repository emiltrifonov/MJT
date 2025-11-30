package bg.sofia.uni.fmi.mjt.imagekit.algorithm;

public class Algorithm {

    public static final int R_SHIFT = 16;
    public static final int G_SHIFT = 8;

    public static final int MASK = 0xff;

    public static final double R_MULT = 0.21;
    public static final double G_MULT = 0.72;
    public static final double B_MULT = 0.07;

    public static final int UNSIGNED_BYTE_MAX = 255;

    public static int getRgbFromPixel(int pixelValue) {
        return ((pixelValue << Algorithm.R_SHIFT) |
                (pixelValue << Algorithm.G_SHIFT) |
                pixelValue);
    }

}
