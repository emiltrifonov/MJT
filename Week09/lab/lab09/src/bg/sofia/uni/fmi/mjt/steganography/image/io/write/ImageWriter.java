package bg.sofia.uni.fmi.mjt.steganography.image.io.write;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

public class ImageWriter {

    public static void write(BufferedImage image, Path imageFile, String dir) {
        if (image == null || dir == null || dir.isBlank()) {
            throw new IllegalArgumentException("Arguments cannot be null or blank.");
        }

        try {
            ImageIO.write(
                    image,
                    "png",
                    new File(dir + File.separator + imageFile.getFileName())
            );
        } catch (IOException e) {
            throw new UncheckedIOException("Error occurred while writing image.", e);
        }
    }

}
