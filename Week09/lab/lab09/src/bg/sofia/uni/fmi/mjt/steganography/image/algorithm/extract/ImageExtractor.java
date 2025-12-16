package bg.sofia.uni.fmi.mjt.steganography.image.algorithm.extract;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

public interface ImageExtractor {

    BufferedImage extract(Path image);

}
