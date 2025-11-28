package bg.sofia.uni.fmi.mjt.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FileTest {

    @Test
    void testFileConstructorThrowsIllegalArgumentExceptionWhenArgumentIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new File(null));
    }

}