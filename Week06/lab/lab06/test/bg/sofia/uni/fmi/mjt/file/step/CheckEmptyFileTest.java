package bg.sofia.uni.fmi.mjt.file.step;

import bg.sofia.uni.fmi.mjt.file.File;
import bg.sofia.uni.fmi.mjt.file.exception.EmptyFileException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckEmptyFileTest {

    @Test
    void testProcessThrowsEmptyFileExceptionWhenFileIsNull() {
        assertThrows(EmptyFileException.class, () -> new CheckEmptyFile().process(null));
    }

    @Test
    void testProcessThrowsEmptyFileExceptionWhenFileContentIsNull() {
        File f = new File("");
        f.setContent(null);
        assertThrows(EmptyFileException.class, () -> new CheckEmptyFile().process(f));
    }

    @Test
    void testProcessThrowsEmptyFileExceptionWhenFileContentIsEmpty() {
        File f = new File("");
        assertThrows(EmptyFileException.class, () -> new CheckEmptyFile().process(f));
    }

    @Test
    void testProcessReturnSameNonEmptyFile() {
        File f = new File("content");
        assertSame(f, new CheckEmptyFile().process(f));
    }
}