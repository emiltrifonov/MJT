package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalFileSystemImageManagerTest {

    private final LocalFileSystemImageManager manager = new LocalFileSystemImageManager();

    @TempDir
    Path tempDir;

    private File createImage(Path path, String format) throws IOException {
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 10, 10);
        g.dispose();

        File f = path.toFile();
        ImageIO.write(img, format, f);
        return f;
    }

    @Test
    void testLoadImageThrowsIllegalArgumentExceptionWhenFileIsNull() {
        assertThrows(IllegalArgumentException.class, () -> manager.loadImage(null));
    }

    @Test
    void testLoadImageThrowsIOExceptionFileDoesNotExist() {
        File missing = tempDir.resolve("missing.png").toFile();
        assertThrows(IOException.class, () -> manager.loadImage(missing));
    }

    @Test
    void testLoadImageThrowsIOExceptionWhenFileIsNotAFile() {
        File directory = tempDir.toFile();
        assertThrows(IOException.class, () -> manager.loadImage(directory));
    }

    @Test
    void testLoadImageThrowsIOExceptionWhenFormatIsInvalid() throws IOException {
        File txtFile = tempDir.resolve("not_image.txt").toFile();
        Files.writeString(txtFile.toPath(), "not an image");
        assertThrows(IOException.class, () -> manager.loadImage(txtFile));
    }

    @Test
    void testLoadImageSuccess() throws IOException {
        File img = createImage(tempDir.resolve("test.png"), "png");
        BufferedImage loaded = manager.loadImage(img);

        assertNotNull(loaded);
        assertEquals(10, loaded.getWidth());
    }

    @Test
    void testLoadImagesFromDirectoryThrowsIllegalArgumentExceptionWhenDirectoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> manager.loadImagesFromDirectory(null));
    }

    @Test
    void testLoadImagesFromDirectoryThrowsIOExceptionWhenDirectoryDoesNotExist() {
        File missing = tempDir.resolve("nonexistent-dir").toFile();
        assertThrows(IOException.class, () -> manager.loadImagesFromDirectory(missing));
    }

    @Test
    void testLoadImagesFromDirectoryThrowsIOExceptionWhenDirectoryIsNotADirectory() throws IOException {
        File file = tempDir.resolve("notDir.png").toFile();
        createImage(file.toPath(), "png");

        assertThrows(IOException.class, () -> manager.loadImagesFromDirectory(file));
    }

    @Test
    void testLoadImagesFromDirectorySuccess() throws IOException {
        File img1 = createImage(tempDir.resolve("a.png"), "png");
        File img2 = createImage(tempDir.resolve("b.jpg"), "jpg");

        List<BufferedImage> images = manager.loadImagesFromDirectory(tempDir.toFile());

        assertEquals(2, images.size());
    }

    @Test
    void testSaveImageThrowsIllegalArgumentExceptionWhenImageIsNull() {
        File f = tempDir.resolve("img.png").toFile();
        assertThrows(IllegalArgumentException.class, () -> manager.saveImage(null, f));
    }

    @Test
    void testSaveImageThrowsIllegalArgumentExceptionWhenFileIsNull() {
        BufferedImage img = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        assertThrows(IllegalArgumentException.class, () -> manager.saveImage(img, null));
    }

    @Test
    void testSaveImageThrowsIOExceptionWhenFileAlreadyExists() throws IOException {
        File f = createImage(tempDir.resolve("exists.png"), "png");

        BufferedImage img = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);

        assertThrows(IOException.class, () -> manager.saveImage(img, f));
    }

    @Test
    void testSaveImageSuccess() throws IOException {
        BufferedImage img = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);

        File output = tempDir.resolve("saved.bmp").toFile();
        manager.saveImage(img, output);

        assertTrue(output.exists());
        BufferedImage loaded = ImageIO.read(output);
        assertNotNull(loaded);
    }
}
