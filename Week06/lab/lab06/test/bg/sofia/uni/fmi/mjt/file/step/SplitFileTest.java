package bg.sofia.uni.fmi.mjt.file.step;

import bg.sofia.uni.fmi.mjt.file.File;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SplitFileTest {

    @Test
    void testProcessThrowsIllegalArgumentExceptionWhenFileIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new SplitFile().process(null));
    }

    @Test
    void testProcessThrowsIllegalArgumentExceptionWhenFileContentIsNull() {
        File f = new File("");
        f.setContent(null);
        assertThrows(IllegalArgumentException.class, () -> new SplitFile().process(f));
    }

    @Test
    void testProcessWithBlankFileContent() {
        String word = "";
        File f = new File(word);

        Set<File> expectedSet = new HashSet<>();
        expectedSet.add(new File(""));

        var actualSet = new SplitFile().process(f);

        assertTrue(expectedSet.containsAll(actualSet) && actualSet.containsAll(expectedSet));
    }

    @Test
    void testProcessWithFileWithOnlyWhitespace() {
        String word = "     ";
        File f = new File(word);

        Set<File> expectedSet = Collections.emptySet();

        var actualSet = new SplitFile().process(f);

        assertTrue(expectedSet.containsAll(actualSet) && actualSet.containsAll(expectedSet));
    }

    @Test
    void testProcessWithFileWithOneWord() {
        String word = "words";
        File f = new File(word);

        Set<File> expectedSet = new HashSet<>();
        expectedSet.add(new File(word));

        var actualSet = new SplitFile().process(f);

        assertTrue(expectedSet.containsAll(actualSet) && actualSet.containsAll(expectedSet));
    }

    @Test
    void testProcessWithFileWithMultipleWords() {
        String word1 = "multiple";
        String word2 = "words";
        File f = new File(word1 + " " + word2);

        Set<File> expectedSet = new HashSet<>();
        expectedSet.add(new File(word1));
        expectedSet.add(new File(word2));

        var actualSet = new SplitFile().process(f);

        assertTrue(expectedSet.containsAll(actualSet) && actualSet.containsAll(expectedSet));
    }
}