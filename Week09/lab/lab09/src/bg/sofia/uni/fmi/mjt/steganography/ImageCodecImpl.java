package bg.sofia.uni.fmi.mjt.steganography;

import bg.sofia.uni.fmi.mjt.steganography.image.algorithm.embed.ImageEmbedder;
import bg.sofia.uni.fmi.mjt.steganography.image.algorithm.embed.LMSBEmbedder;
import bg.sofia.uni.fmi.mjt.steganography.image.algorithm.extract.ImageExtractor;
import bg.sofia.uni.fmi.mjt.steganography.image.algorithm.extract.LMSBExtractor;
import bg.sofia.uni.fmi.mjt.steganography.image.io.read.ImageReader;
import bg.sofia.uni.fmi.mjt.steganography.image.io.write.ImageWriter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ImageCodecImpl implements ImageCodec {

    private static final ImageEmbedder IMAGE_EMBEDDER = new LMSBEmbedder();
    private static final ImageExtractor IMAGE_EXTRACTOR = new LMSBExtractor();

    @Override
    public void embedPNGImages(String coverSourceDirectory, String secretSourceDirectory, String outputDirectory) {
        createOutputDirectory(outputDirectory);

        List<Path> coverImagesPaths = ImageReader.readImagesFromDirLexicographically(coverSourceDirectory);
        List<Path> secretImagesPaths = ImageReader.readImagesFromDirLexicographically(secretSourceDirectory);

        for (int i = 0; i < coverImagesPaths.size(); i++) {
            Path coverImagePath = coverImagesPaths.get(i);
            BufferedImage embedded = IMAGE_EMBEDDER.embed(coverImagePath, secretImagesPaths.get(i));
            ImageWriter.write(embedded, coverImagePath, outputDirectory);
        }
    }

    @Override
    public void extractPNGImages(String sourceDirectory, String outputDirectory) {
        createOutputDirectory(outputDirectory);

        List<Path> imagesPaths = ImageReader.readImagesFromDir(sourceDirectory);

        for (Path imagePath : imagesPaths) {
            BufferedImage image = IMAGE_EXTRACTOR.extract(imagePath);
            ImageWriter.write(image, imagePath, outputDirectory);
        }
    }

    private void createOutputDirectory(String outputDirectory) {
        try {
            Files.createDirectories(Path.of(outputDirectory));
        } catch (IOException e) {
            throw new UncheckedIOException("Error occurred while creating output directory.", e);
        }
    }

}
