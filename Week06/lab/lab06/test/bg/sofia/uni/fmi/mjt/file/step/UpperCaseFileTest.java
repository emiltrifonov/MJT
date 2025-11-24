package bg.sofia.uni.fmi.mjt.file.step;

import bg.sofia.uni.fmi.mjt.file.File;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UpperCaseFileTest {

    @Test
    void testProcessThrowsIllegalArgumentExceptionWhenFileIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new UpperCaseFile().process(null));
    }

    @Test
    void testProcessThrowsIllegalArgumentExceptionWhenFileContentIsNull() {
        File f = new File("");
        f.setContent(null);
        assertThrows(IllegalArgumentException.class, () -> new UpperCaseFile().process(f));
    }

    @Test
    void testProcessWithMixedCaseFileContent() {
        File f = new File("Aa");
        assertEquals("AA", new UpperCaseFile().process(f).getContent());
    }

    @Test
    void testProcessWithBlankFileContent() {
        File f = new File("");
        assertEquals("", new UpperCaseFile().process(f).getContent());
    }

}