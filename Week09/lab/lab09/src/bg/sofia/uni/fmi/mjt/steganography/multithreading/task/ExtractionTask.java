package bg.sofia.uni.fmi.mjt.steganography.multithreading.task;

import java.nio.file.Path;

public class ExtractionTask extends Task {
    private final Path image;
    private final String directory;

    public ExtractionTask(Path image, String directory) {
        this.image = image;
        this.directory = directory;
    }

    public Path getImage() {
        return image;
    }

    public String getDirectory() {
        return directory;
    }
}
