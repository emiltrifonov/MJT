package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.Algorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;

import java.awt.image.BufferedImage;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm {

    private final ImageAlgorithm grayscaleAlgorithm;

    private static final int KERNEL_SIZE = 3;

    private static final int[][] GX = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
    };

    private static final int[][] GY = {
            {-1, -2, -1},
            {0, 0, 0},
            {1, 2, 1}
    };

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm) {
        this.grayscaleAlgorithm = grayscaleAlgorithm;
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null.");
        }

        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage grayImage = grayscaleAlgorithm.process(image);
        BufferedImage edgesImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int[][] subGrayImage = getSubImageMatrix(grayImage, i - 1, j - 1);

                edgesImage.setRGB(i, j, getNewPixel(subGrayImage));
            }
        }

        return edgesImage;
    }

    private int[][] getSubImageMatrix(BufferedImage image, int x, int y) {
        int[][] subImageMatrix = new int[KERNEL_SIZE][KERNEL_SIZE];

        for (int i = 0; i < KERNEL_SIZE; i++, x++) {
            int yCopy = y;

            for (int j = 0; j < KERNEL_SIZE ; j++, yCopy++) {
                if (isValidPosition(image, x, yCopy)) {
                    int rgb = image.getRGB(x, yCopy);
                    int intensity = rgb & Algorithm.MASK;
                    subImageMatrix[i][j] = intensity;
                } else {
                    subImageMatrix[i][j] = 0;
                }
            }

        }

        return subImageMatrix;
    }

    private boolean isValidPosition(BufferedImage image, int x, int y) {
        int w = image.getWidth();
        int h = image.getHeight();

        return x >= 0 && x < w && y >= 0 && y < h;
    }

    private int getNewPixel(int[][] selection) {
        double g = getG(selection);

        int pixelValue = Math.min(Algorithm.UNSIGNED_BYTE_MAX, (int)Math.round(g));

        return Algorithm.getRgbFromPixel(pixelValue);
    }

    private double getG(int[][] selection) {
        int gxChange = getGxChange(selection);
        int gyChange = getGyChange(selection);

        return Math.sqrt(gxChange * gxChange + gyChange * gyChange);
    }

    private int getGxChange(int[][] selection) {
        return getMatrixSum(getElementByElementMatrixProduct(selection, GX));
    }

    private int getGyChange(int[][] selection) {
        return getMatrixSum(getElementByElementMatrixProduct(selection, GY));
    }

    private int getMatrixSum(int[][] matrix) {
        int sum = 0;

        for (int[] row : matrix) {
            for (int el : row) {
                sum += el;
            }
        }

        return sum;
    }

    /**
     * Always processes two 3x3 matrices and nothing else
     */
    private int[][] getElementByElementMatrixProduct(int[][] first, int[][] second) {
        int[][] product = new int[KERNEL_SIZE][KERNEL_SIZE];

        for (int i = 0; i < KERNEL_SIZE; i++) {
            for (int j = 0; j < KERNEL_SIZE; j++) {
                product[i][j] = first[i][j] * second[i][j];
            }
        }

        return product;
    }

}
