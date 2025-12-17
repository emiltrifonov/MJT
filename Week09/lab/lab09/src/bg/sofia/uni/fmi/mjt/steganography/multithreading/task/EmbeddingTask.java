package bg.sofia.uni.fmi.mjt.steganography.multithreading.task;

import java.nio.file.Path;

public class EmbeddingTask extends Task {
    private final Path cover;
    private final Path secret;
    private final String directory;

    public EmbeddingTask(Path cover, Path secret, String directory) {
        this.cover = cover;
        this.secret = secret;
        this.directory = directory;
    }

    public Path getCover() {
        return cover;
    }

    public Path getSecret() {
        return secret;
    }

    public String getDirectory() {
        return directory;
    }
}
