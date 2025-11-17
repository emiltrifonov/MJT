package bg.sofia.uni.fmi.mjt.file.step;

import bg.sofia.uni.fmi.mjt.file.File;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

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

        assertSame(fileCollection, printFiles.process(fileCollection));
    }

    @Test
    void testProcessReturnsSameCollection() {
        Collection<File> fileCollection = new HashSet<>();
        File f1 = new File("file1");
        File f2 = new File("file2");
        fileCollection.add(f1);
        fileCollection.add(f2);

        PrintFiles printFiles = new PrintFiles();

        assertSame(fileCollection, printFiles.process(fileCollection));
    }
}