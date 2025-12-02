package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LocalFileSystemImageManager implements FileSystemImageManager {

    private static final Set<String> VALID_IMAGE_FORMATS = Set.of("jpeg", "jpg", "png", "bmp");

    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        if (imageFile == null) {
            throw new IllegalArgumentException("Image file cannot be null.");
        }

        validateImageFile(imageFile);

        return ImageIO.read(imageFile);
    }

    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {
        if (imagesDirectory == null) {
            throw new IllegalArgumentException("Images directory cannot be null.");
        }

        validateImagesDirectory(imagesDirectory);

        List<BufferedImage> bufferedImages = new ArrayList<>();

        try (var imagesPaths = Files.newDirectoryStream(imagesDirectory.toPath())) {
            for (Path imagePath : imagesPaths) {
                bufferedImages.add(loadImage(imagePath.toFile()));
            }

        }

        return bufferedImages;
    }

    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null.");
        }
        if (imageFile == null) {
            throw new IllegalArgumentException("Image file cannot be null.");
        }

        if (imageFile.exists()) {
            throw new IOException("Image file already exists.");
        }

        // JavaDoc doesn't specify that we need to test
        // imageFile for format correctness, so it is
        // assumed that the format is always valid
        String format = getFormat(imageFile);

        ImageIO.write(image, format, imageFile);
    }

    private void validateImageFile(File imageFile) throws IOException {
        if (!imageFile.exists()) {
            throw new IOException("Image file does not exist.");
        }

        if (!imageFile.isFile()) {
            throw new IOException("Image file is not a file.");
        }

        if (!isValidFormat(getFormat(imageFile))) {
            throw new IOException("Invalid image file format.");
        }
    }

    private String getFormat(File file) {
        String name = file.getName();
        int index = name.lastIndexOf('.');

        return index == -1 ? "" : name.substring(index + 1);
    }

    private boolean isValidFormat(String format) {
        return VALID_IMAGE_FORMATS.contains(format);
    }

    private void validateImagesDirectory(File imagesDirectory) throws IOException {
        if (!imagesDirectory.exists()) {
            throw new IOException("Images directory does not exist.");
        }

        if (!imagesDirectory.isDirectory()) {
            throw new IOException("Images directory is not a directory.");
        }
    }

}
