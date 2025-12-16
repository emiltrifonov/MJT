package bg.sofia.uni.fmi.mjt.steganography.image.algorithm.extract;

import bg.sofia.uni.fmi.mjt.steganography.image.bits.PixelBitOperations;
import bg.sofia.uni.fmi.mjt.steganography.image.algorithm.ImageAlgorithm;
import bg.sofia.uni.fmi.mjt.steganography.image.io.read.ImageReader;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

public class LMSBExtractor extends ImageAlgorithm implements ImageExtractor {

    @Override
    public BufferedImage extract(Path imagePath) {
        BufferedImage image = ImageReader.readImage(imagePath);
        int width = extractWidth(image);
        int height = extractHeight(image);

        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        fillNewImage(image, resultImage);

        return resultImage;
    }

    private void fillNewImage(BufferedImage oldImage, BufferedImage newImage) {
        int oldW = oldImage.getWidth();
        int newW = newImage.getWidth();
        int newH = newImage.getHeight();

        int totalIndex = PIXELS_PER_DIMENSION * 2;

        for (int newY = 0; newY < newH; newY++) {
            for (int newX = 0; newX < newW; newX++) {
                int oldX = totalIndex % oldW;
                int oldY = totalIndex / oldW;

                int pixel = oldImage.getRGB(oldX, oldY);
                newImage.setRGB(newX, newY, getExtractedPixel(pixel));

                totalIndex++;
            }
        }
    }

    private int getExtractedPixel(int oldPixel) {
        int gray = getGray(oldPixel);

        return PixelBitOperations.distributeGrayToRGB(gray);
    }

    private int getGray(int pixel) {
        int r = PixelBitOperations.getRedBit(pixel);
        int g = PixelBitOperations.getGreenBit(pixel);
        int b = PixelBitOperations.getBlueBit(pixel);

        return PixelBitOperations.putBitsInMSB(r, g, b);
    }

    private int extractWidth(BufferedImage image) {
        return extractDimension(image, WIDTH_PIXELS_SHIFT);
    }

    private int extractHeight(BufferedImage image) {
        return extractDimension(image, HEIGHT_PIXELS_SHIFT);
    }

    private int extractDimension(BufferedImage image, int shifts) {
        int dim = 0;
        int w = image.getWidth();
        int h = image.getHeight();
        int bitCounter = 0;

        for (int i = 0; i < h && bitCounter < BITS_IN_DIMENSION_REPRESENTATION; i++) {
            for (int j = 0; j < w && bitCounter < BITS_IN_DIMENSION_REPRESENTATION; j++) {
                if (--shifts >= 0) {
                    continue;
                }

                int pixel = image.getRGB(j, i);

                dim |= get3LSBs(pixel, BITS_IN_DIMENSION_REPRESENTATION - bitCounter - 1);

                bitCounter += BIT_BATCH_SIZE;
            }
        }

        return dim;
    }

    int get3LSBs(int pixel, int firstBitShifts) {
        int r = PixelBitOperations.getRedBit(pixel);
        int g = PixelBitOperations.getGreenBit(pixel);
        int b = PixelBitOperations.getBlueBit(pixel);

        return PixelBitOperations.putBitsInMSB(r, g, b, firstBitShifts);
    }

}
