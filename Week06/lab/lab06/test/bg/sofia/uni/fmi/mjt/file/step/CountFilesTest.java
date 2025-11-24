package bg.sofia.uni.fmi.mjt.file.step;

import bg.sofia.uni.fmi.mjt.file.File;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountFilesTest {

    @Test
    void testProcessThrowsIllegalArgumentExceptionWhenCollectionIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new CountFiles().process(null));
    }

    @Test
    void testProcessWithEmptyCollection() {
        assertEquals(0, new CountFiles().process(Collections.emptyList()));
    }

    @Test
    void testProcessWithNonEmptyCollection() {
        assertEquals(1, new CountFiles().process(List.of(new File(""))));
    }

}