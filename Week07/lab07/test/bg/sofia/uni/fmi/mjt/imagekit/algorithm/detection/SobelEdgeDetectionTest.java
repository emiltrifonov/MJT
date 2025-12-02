package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.GrayscaleAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SobelEdgeDetectionTest {

    GrayscaleAlgorithm grayscaleAlgorithm = new LuminosityGrayscale();
    EdgeDetectionAlgorithm edgeDetectionAlgorithm = new SobelEdgeDetection(grayscaleAlgorithm);

    @Test
    void testProcessThrowsIllegalArgumentExceptionWhenImageIsNull() {
        assertThrows(IllegalArgumentException.class, () -> edgeDetectionAlgorithm.process(null));
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

        int[][] sobel = {
                { 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF },
                { 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF },
                { 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF }
        };

        BufferedImage sobelImage = edgeDetectionAlgorithm.process(image);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(sobel[i][j], sobelImage.getRGB(j, i));
            }
        }
    }


}