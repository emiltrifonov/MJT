package bg.sofia.uni.fmi.mjt.steganography.image.io.read;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ImageReader {

    public static BufferedImage readImage(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null.");
        }

        try {
            return ImageIO.read(path.toFile());
        } catch (IOException e) {
            throw new UncheckedIOException("Error occurred while reading image.", e);
        }
    }

    public static List<Path> readImagesFromDirLexicographically(String directory) {
        List<Path> images = readImagesFromDir(directory);

        images.sort(Comparator.comparing(p -> p.getFileName().toString()));

        return images;
    }

    public static List<Path> readImagesFromDir(String directory) {
        if (directory == null || directory.isBlank()) {
            throw new IllegalArgumentException("Directory cannot be null or blank.");
        }

        return getImages(directory);
    }

    private static List<Path> getImages(String directory) {
        List<Path> images = new ArrayList<>();

        try (var paths = Files.newDirectoryStream(Path.of(directory))) {
            for (Path path : paths) {
                if (isPNG(path)) {
                    images.add(path);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error occurred while reading images.", e);
        }

        return images;
    }

    private static boolean isPNG(Path path) {
        return path.getFileName().toString().endsWith(".png");
    }

}
