package bg.sofia.uni.fmi.mjt.steganography.image.algorithm.embed;

import bg.sofia.uni.fmi.mjt.steganography.image.bits.PixelBitOperations;
import bg.sofia.uni.fmi.mjt.steganography.image.algorithm.ImageAlgorithm;
import bg.sofia.uni.fmi.mjt.steganography.image.io.read.ImageReader;
import java.awt.image.BufferedImage;
import java.nio.file.Path;

public class LMSBEmbedder extends ImageAlgorithm implements ImageEmbedder {

    @Override
    public BufferedImage embed(Path cover, Path secret) {
        BufferedImage coverImage = ImageReader.readImage(cover);
        BufferedImage secretImage = ImageReader.readImage(secret);

        embedDimensions(coverImage, secretImage);
        embedActualImage(coverImage, secretImage);

        return coverImage;
    }

    private void embedActualImage(BufferedImage cover, BufferedImage secret) {
        int totalIndex = PIXELS_PER_DIMENSION * 2;
        int coverW = cover.getWidth();

        for (int y = 0; y < secret.getHeight(); y++) {
            for (int x = 0; x < secret.getWidth(); x++) {
                int coverX = totalIndex % coverW;
                int coverY = totalIndex / coverW;

                int secretPixel = secret.getRGB(x, y);
                int coverPixel = cover.getRGB(coverX, coverY);
                cover.setRGB(coverX, coverY, getEmbeddedPixel(secretPixel, coverPixel));

                totalIndex++;
            }
        }
    }

    private int getEmbeddedPixel(int secretPixel, int coverPixel) {
        int avg = avgMethodGrayscale(secretPixel);

        int r = PixelBitOperations.getRedBitFromGray(avg);
        int g = PixelBitOperations.getGreenBitFromGray(avg);
        int b = PixelBitOperations.getBlueBitFromGray(avg);

        coverPixel = PixelBitOperations.toggleLSBsOfRGB(coverPixel, r, g, b);

        return coverPixel;
    }

    private int avgMethodGrayscale(int pixel) {
        int r = PixelBitOperations.getRedByte(pixel);
        int g = PixelBitOperations.getGreenByte(pixel);
        int b = PixelBitOperations.getBlueByte(pixel);

        return (r + g + b) / RGB_CHANNEL_COUNT;
    }

    private void embedDimensions(BufferedImage cover, BufferedImage secret) {
        embedWidth(cover, secret);
        embedHeight(cover, secret);
    }

    private void embedWidth(BufferedImage cover, BufferedImage secret) {
        embedDimension(cover, secret.getWidth(), WIDTH_PIXELS_SHIFT);
    }

    private void embedHeight(BufferedImage cover, BufferedImage secret) {
        embedDimension(cover, secret.getHeight(), HEIGHT_PIXELS_SHIFT);
    }

    private void embedDimension(BufferedImage cover, int dim, int shifts) {
        int bitShifts = BITS_IN_DIMENSION_REPRESENTATION - 1;

        for (int i = 0; i < cover.getHeight() && bitShifts >= 0; i++) {
            for (int j = 0; j < cover.getWidth() && bitShifts >= 0; j++) {
                if (--shifts >= 0) {
                    continue;
                }

                int pixel = cover.getRGB(j, i);

                int r = PixelBitOperations.getBit(dim, bitShifts--);
                int g = PixelBitOperations.getBit(dim, bitShifts--);
                int b = PixelBitOperations.getBit(dim, bitShifts--);

                pixel = PixelBitOperations.toggleLSBsOfRGB(pixel, r, g, b);

                cover.setRGB(j, i, pixel);
            }
        }
    }

}
