package bg.sofia.uni.fmi.mjt.file.step;

import bg.sofia.uni.fmi.mjt.file.File;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountFilesTest {

    @Test
    void testProcessThrowsIllegalArgumentExceptionWhenCollectionIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new CountFiles().process(null));
    }

    @Test
    void testProcessWithEmptyCollection() {
        assertEquals(0, new CountFiles().process(Collections.emptySet()));
    }

    @Test
    void testProcessWithNonEmptyCollection() {
        Collection<File> fileCollection = new HashSet<>();
        fileCollection.add(new File(""));

        assertEquals(fileCollection.size(), new CountFiles().process(fileCollection));
    }

}