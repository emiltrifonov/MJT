package bg.sofia.uni.fmi.mjt.steganography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageCodecImpl implements ImageCodec {

    @Override
    public void embedPNGImages(String coverSourceDirectory, String secretSourceDirectory, String outputDirectory) {

    }

    @Override
    public void extractPNGImages(String sourceDirectory, String outputDirectory) {

        try (var paths = Files.newDirectoryStream(Path.of(sourceDirectory))) {

            for (var path : paths) {
                if (path.getFileName().toString().endsWith(".png")) {
                    extract(path, outputDirectory);
                }
            }

        } catch (IOException e) {
            throw new UncheckedIOException("Error occurred while iterating through source directory.", e);
        }

    }

    private void extract(Path imagePath, String outputDirectory) throws IOException {
        BufferedImage image = ImageIO.read(imagePath.toFile());
        int width = extractWidth(image);
        int height = extractHeight(image);

        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        fillNewImage(image, resultImage);

        ImageIO.write(resultImage, "png",
                new File(outputDirectory + File.separator + imagePath.getFileName()));
    }

    private void fillNewImage(BufferedImage oldImage, BufferedImage newImage) {
        int oldW = oldImage.getWidth(), oldH = oldImage.getHeight();
        int newW = newImage.getWidth(), newH = newImage.getHeight();

        int totalIndex = 8;

        for (int newY = 0; newY < newH; newY++) {
            for (int newX = 0; newX < newW; newX++) {

                int oldX = totalIndex % oldW;
                int oldY = totalIndex / oldW;

                int pixel = oldImage.getRGB(oldX, oldY);
                newImage.setRGB(newX, newY, getNewPixel(pixel));

                totalIndex++;
            }
        }
    }

    int getNewPixel(int oldPixel) {
        int pixel = 0;
        int gray = 0;

        int rBit = (oldPixel >> 16) & 1;
        gray |= (rBit << 7);
        int gBit = (oldPixel >> 8) & 1;
        gray |= (gBit << 6);
        int bBit = oldPixel & 1;
        gray |= (bBit << 5);

        pixel |= (gray << 16);
        pixel |= (gray << 8);
        pixel |= gray;

        return pixel;
    }

    private int extractWidth(BufferedImage image) {
        return extractDimension(image, 0);
    }

    private int extractHeight(BufferedImage image) {
        return extractDimension(image, 4);
    }

    private int extractDimension(BufferedImage image, int startFrom) {
        int dim = 0;
        int w = image.getWidth();
        int h = image.getHeight();

        final int bitCount = 12;
        final int bitIncrement = 3;
        int bitCounter = 0;
        for (int i = 0; i < h && bitCounter < bitCount; i++) {
            for (int j = 0; j < w && bitCounter < bitCount; j++) {
                if (--startFrom >= 0) {
                    continue;
                }

                int pixel = image.getRGB(j, i);

                dim |= get3LSBs(pixel, bitCount - bitCounter - 1);

                bitCounter += bitIncrement;
            }
        }

        return dim;
    }

    int get3LSBs(int number, int firstBitShifts) {
        int result = 0;

        int firstBit = ((number >> 16) & 1);
        int secondBit = ((number >> 8) & 1);
        int thirdBit = (number & 1);

        result |= (firstBit << firstBitShifts--);
        result |= (secondBit << firstBitShifts--);
        result |= (thirdBit << firstBitShifts);

        return result;
    }

}
