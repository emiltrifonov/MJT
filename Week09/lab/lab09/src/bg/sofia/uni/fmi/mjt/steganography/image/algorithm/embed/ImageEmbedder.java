package bg.sofia.uni.fmi.mjt.steganography.image.algorithm.embed;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

public interface ImageEmbedder {

    BufferedImage embed(Path cover, Path secret);

}
