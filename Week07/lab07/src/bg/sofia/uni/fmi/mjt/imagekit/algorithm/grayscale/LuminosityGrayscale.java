package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.Algorithm;

import java.awt.image.BufferedImage;

public class LuminosityGrayscale implements GrayscaleAlgorithm {

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null.");
        }

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                int newRgb = transform(rgb);

                newImage.setRGB(i, j, newRgb);
            }
        }

        return newImage;
    }

    private int transform(int rgb) {
        int r = getR(rgb);
        int g = getG(rgb);
        int b = getB(rgb);

        int grayPixel = Math.clamp((int)(Math.round(
                Algorithm.R_MULT * r + Algorithm.G_MULT * g + Algorithm.B_MULT * b)),
                0, Algorithm.UNSIGNED_BYTE_MAX);

        return Algorithm.getRgbFromPixel(grayPixel);
    }

    private int getR(int rgb) {
        return ((rgb >> Algorithm.R_SHIFT) & Algorithm.MASK);
    }

    private int getG(int rgb) {
        return ((rgb >> Algorithm.G_SHIFT) & Algorithm.MASK);
    }

    private int getB(int rgb) {
        return (rgb & Algorithm.MASK);
    }

}
