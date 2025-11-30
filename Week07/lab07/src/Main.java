import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection.SobelEdgeDetection;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import bg.sofia.uni.fmi.mjt.imagekit.filesystem.FileSystemImageManager;
import bg.sofia.uni.fmi.mjt.imagekit.filesystem.LocalFileSystemImageManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    static void main() throws IOException  {
        FileSystemImageManager fsImageManager = new LocalFileSystemImageManager();

        BufferedImage imageKitten = fsImageManager.loadImage(new File("kitten.png"));
        BufferedImage imageCar = fsImageManager.loadImage(new File("car.jpg"));
        BufferedImage imageNiko = fsImageManager.loadImage(new File("niko.jpg"));
        BufferedImage imageTatiDqdo = fsImageManager.loadImage(new File("tatiidqdo.jpg"));

        ImageAlgorithm grayscaleAlgorithm = new LuminosityGrayscale();
        BufferedImage grayscaleImageKitten = grayscaleAlgorithm.process(imageKitten);
        BufferedImage grayscaleImageCar = grayscaleAlgorithm.process(imageCar);
        BufferedImage grayscaleImageNiko = grayscaleAlgorithm.process(imageNiko);
        BufferedImage grayscaleImageTatiDqdo = grayscaleAlgorithm.process(imageTatiDqdo);

        ImageAlgorithm sobelEdgeDetection = new SobelEdgeDetection(grayscaleAlgorithm);
        BufferedImage edgeDetectedImageKitten = sobelEdgeDetection.process(imageKitten);
        BufferedImage edgeDetectedImageCar = sobelEdgeDetection.process(imageCar);
        BufferedImage edgeDetectedNiko = sobelEdgeDetection.process(imageNiko);
        BufferedImage edgeDetectedTatiDqdo = sobelEdgeDetection.process(imageTatiDqdo);

        //fsImageManager.saveImage(grayscaleImageKitten, new File("kitten-grayscale.png"));
        //fsImageManager.saveImage(grayscaleImageCar, new File("car-grayscale.png"));
        //fsImageManager.saveImage(grayscaleImageNiko, new File("niko-grayscale.png"));
        fsImageManager.saveImage(grayscaleImageTatiDqdo, new File("tatiidqdo-grayscale.png"));

        //fsImageManager.saveImage(edgeDetectedImageKitten, new File("kitten-edge-detected.png"));
        //fsImageManager.saveImage(edgeDetectedImageCar, new File("car-edge-detected.png"));
        //fsImageManager.saveImage(edgeDetectedNiko, new File("niko-edge-detected.png"));
        fsImageManager.saveImage(edgeDetectedTatiDqdo, new File("tatiidqdo-edge-detected.png"));
    }
}
