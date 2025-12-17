package bg.sofia.uni.fmi.mjt.steganography;

public class Main {
    static void main() {
        var codec = new ImageCodecImpl();

        codec.extractPNGImages(".", "./outputDir");

        codec.embedPNGImages("./coverDir", "./secretDir",
                "./embedOutput");

        codec.extractPNGImages("./embedOutput", "./outputDir");
    }
}
