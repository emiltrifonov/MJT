package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LuminosityGrayscaleTest {

    GrayscaleAlgorithm grayscaleAlgorithm = new LuminosityGrayscale();

    @Test
    void testProcessThrowsIllegalArgumentExceptionWhenImageIsNull() {
        assertThrows(IllegalArgumentException.class, () -> grayscaleAlgorithm.process(null));
    }

    @Test
    void testProcessReturnsCorrectlyProcessedImage() {
        int[][] matrix = {
                { 0x0A141E, 0x326496, 0xC8B4A0 },
                { 0x1E3C5A, 0x785028, 0xFFC864 },
                { 0x0080FF, 0x5A2D0A, 0xDCDCDC }
        };

        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                image.setRGB(j, i, matrix[i][j]);
            }
        }

        int[][] gray = {
                { 0xFF131313, 0xFF5D5D5D, 0xFFB7B7B7 },
                { 0xFF383838, 0xFF565656, 0xFFCDCDCD },
                { 0xFF6E6E6E, 0xFF343434, 0xFFDCDCDC }
        };

        BufferedImage grayImage = grayscaleAlgorithm.process(image);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(gray[i][j], grayImage.getRGB(j, i));
            }
        }
    }

}