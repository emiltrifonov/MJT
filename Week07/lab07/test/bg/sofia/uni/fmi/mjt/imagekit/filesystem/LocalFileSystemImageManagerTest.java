package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Maybe also add tests with temp files and temp dirs or something
 */
class LocalFileSystemImageManagerTest {

    private final LocalFileSystemImageManager manager = new LocalFileSystemImageManager();

    @Test
    void testLoadImageThrowsIllegalArgumentExceptionWhenImageIsNull() {
        assertThrows(IllegalArgumentException.class, () -> manager.loadImage(null));
    }

    @Test
    void testLoadImagesFromDirectoryThrowsIllegalArgumentExceptionWhenImagesDirectoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> manager.loadImagesFromDirectory(null));
    }

    @Test
    void testSaveImageIllegalArgumentExceptionWhenImageIsNull() {
        assertThrows(IllegalArgumentException.class, () -> manager.saveImage(null, null));
    }

    @Test
    void testSaveImageIllegalArgumentExceptionWhenImageFileIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                manager.saveImage(new BufferedImage(0,0, BufferedImage.TYPE_INT_RGB), null));
    }

}