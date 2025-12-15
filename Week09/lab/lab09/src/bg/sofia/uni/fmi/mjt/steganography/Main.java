package bg.sofia.uni.fmi.mjt.steganography;

import java.nio.file.Path;

public class Main {
    static void main() {
        var codec = new ImageCodecImpl();
        codec.extractPNGImages(".", "./outputDir");
    }
}
