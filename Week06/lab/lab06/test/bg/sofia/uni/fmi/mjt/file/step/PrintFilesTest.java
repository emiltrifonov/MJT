package bg.sofia.uni.fmi.mjt.file.step;

import bg.sofia.uni.fmi.mjt.file.File;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrintFilesTest {

    @Test
    void testProcessThrowsIllegalArgumentExceptionWhenGivenCollectionIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new PrintFiles().process(null));
    }

    @Test
    void testProcessReturnsSameEmptyCollection() {
        Collection<File> fileCollection = Collections.emptySet();

        PrintFiles printFiles = new PrintFiles();

        Collection<File> returnedCollection = printFiles.process(fileCollection);

        assertSame(fileCollection, returnedCollection);
    }

    @Test
    void testProcessReturnsSameCollection() {
        Collection<File> fileCollection = new ArrayList<>(List.of(new File("file")));

        PrintFiles printFiles = new PrintFiles();

        Collection<File> returnedCollection = printFiles.process(fileCollection);

        assertSame(fileCollection, returnedCollection);
        assertIterableEquals(fileCollection, returnedCollection);
    }
}