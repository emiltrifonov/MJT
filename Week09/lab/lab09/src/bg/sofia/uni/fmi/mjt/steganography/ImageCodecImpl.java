package bg.sofia.uni.fmi.mjt.steganography;

import bg.sofia.uni.fmi.mjt.steganography.image.algorithm.embed.ImageEmbedder;
import bg.sofia.uni.fmi.mjt.steganography.image.algorithm.embed.LMSBEmbedder;
import bg.sofia.uni.fmi.mjt.steganography.image.algorithm.extract.ImageExtractor;
import bg.sofia.uni.fmi.mjt.steganography.image.algorithm.extract.LMSBExtractor;
import bg.sofia.uni.fmi.mjt.steganography.image.io.read.ImageReader;
import bg.sofia.uni.fmi.mjt.steganography.image.io.write.ImageWriter;
import bg.sofia.uni.fmi.mjt.steganography.multithreading.blockingqueue.MyBlockingQueue;
import bg.sofia.uni.fmi.mjt.steganography.multithreading.task.EmbeddingTask;
import bg.sofia.uni.fmi.mjt.steganography.multithreading.task.ExtractionTask;
import bg.sofia.uni.fmi.mjt.steganography.multithreading.task.Task;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ImageCodecImpl implements ImageCodec {

    private static final ImageEmbedder IMAGE_EMBEDDER = new LMSBEmbedder();
    private static final ImageExtractor IMAGE_EXTRACTOR = new LMSBExtractor();
    private static final int CONSUMERS_MAX_COUNT = Runtime.getRuntime().availableProcessors();
    private static final EmbeddingTask EMBEDDING_POISON =
            new EmbeddingTask(null, null, null);
    private static final ExtractionTask EXTRACTION_POISON =
            new ExtractionTask(null, null);

    @Override
    public void embedPNGImages(String coverSourceDirectory, String secretSourceDirectory, String outputDirectory) {
        createOutputDirectory(outputDirectory);

        List<Path> coverImagesPaths = ImageReader.readImagesFromDirLexicographically(coverSourceDirectory);
        List<Path> secretImagesPaths = ImageReader.readImagesFromDirLexicographically(secretSourceDirectory);

        MyBlockingQueue<EmbeddingTask> queue = new MyBlockingQueue<>();
        List<Thread> producers = startEmbeddingProducers(coverImagesPaths, secretImagesPaths,
                outputDirectory, queue);
        List<Thread> consumers = startEmbeddingConsumers(queue);

        joinThreads(producers);

        insertPoisonPills(queue, EMBEDDING_POISON);

        joinThreads(consumers);
    }

    @Override
    public void extractPNGImages(String sourceDirectory, String outputDirectory) {
        createOutputDirectory(outputDirectory);

        List<Path> imagesPaths = ImageReader.readImagesFromDir(sourceDirectory);

        MyBlockingQueue<ExtractionTask> queue = new MyBlockingQueue<>();
        List<Thread> producers = startExtractingProducers(imagesPaths, outputDirectory, queue);

        List<Thread> consumers = startExtractingConsumers(queue);

        joinThreads(producers);

        insertPoisonPills(queue, EXTRACTION_POISON);

        joinThreads(consumers);
    }

    private void createOutputDirectory(String outputDirectory) {
        try {
            Files.createDirectories(Path.of(outputDirectory));
        } catch (IOException e) {
            throw new UncheckedIOException("Error occurred while creating output directory.", e);
        }
    }

    private <T extends Task> void insertPoisonPills(MyBlockingQueue<T> queue, T poisonPill) {
        for (int i = 0; i < CONSUMERS_MAX_COUNT; i++) {
            queue.put(poisonPill);
        }
    }

    private List<Thread> startExtractingProducers(List<Path> imagePaths, String outputDirectory,
                                                  MyBlockingQueue<ExtractionTask> queue) {
        List<Thread> producers = new ArrayList<>();

        for (Path path : imagePaths) {
            producers.add(startExtractingProducer(path, outputDirectory, queue));
        }

        return producers;
    }

    private Thread startExtractingProducer(Path imagePath, String outputDirectory,
                                         MyBlockingQueue<ExtractionTask> queue) {
        Thread producer = new Thread(() -> {
            ExtractionTask extractionTask = new ExtractionTask(imagePath, outputDirectory);
            queue.put(extractionTask);
        });

        producer.start();
        return producer;
    }

    private List<Thread> startExtractingConsumers(MyBlockingQueue<ExtractionTask> queue) {
        List<Thread> consumers = new ArrayList<>();

        for (int i = 0; i < CONSUMERS_MAX_COUNT; i++) {
            consumers.add(startExtractingConsumer(queue));
        }

        return consumers;
    }

    private Thread startExtractingConsumer(MyBlockingQueue<ExtractionTask> queue) {
        Thread consumer = new Thread(() -> {
            while (true) {
                try {
                    ExtractionTask extractionTask = queue.get();

                    if (extractionTask == EXTRACTION_POISON) {
                        return;
                    }

                    BufferedImage extracted = IMAGE_EXTRACTOR.extract(extractionTask.getImage());
                    ImageWriter.write(extracted, extractionTask.getImage(), extractionTask.getDirectory());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });

        consumer.start();
        return consumer;
    }

    private List<Thread> startEmbeddingProducers(List<Path> coverImages, List<Path> secretImages,
                                                 String outputDirectory,
                                                 MyBlockingQueue<EmbeddingTask> queue) {
        List<Thread> producers = new ArrayList<>();

        for (int i = 0; i < coverImages.size(); i++) {
            producers.add(startEmbeddingProducer(coverImages.get(i), secretImages.get(i),
                    outputDirectory, queue));
        }

        return producers;
    }

    private Thread startEmbeddingProducer(Path coverImage, Path secretImage, String outputDirectory,
                                        MyBlockingQueue<EmbeddingTask> queue) {
        Thread producer = new Thread(() -> {
            EmbeddingTask embeddingTask = new EmbeddingTask(coverImage,
                    secretImage, outputDirectory);
            queue.put(embeddingTask);
        });

        producer.start();
        return producer;
    }

    private List<Thread> startEmbeddingConsumers(MyBlockingQueue<EmbeddingTask> queue) {
        List<Thread> consumers = new ArrayList<>();

        for (int i = 0; i < CONSUMERS_MAX_COUNT; i++) {
            consumers.add(startEmbeddingConsumer(queue));
        }

        return consumers;
    }

    private Thread startEmbeddingConsumer(MyBlockingQueue<EmbeddingTask> queue) {
        Thread consumer = new Thread(() -> {
            while (true) {
                try {
                    EmbeddingTask embeddingTask = queue.get();

                    if (embeddingTask == EMBEDDING_POISON) {
                        break;
                    }

                    BufferedImage embedded = IMAGE_EMBEDDER
                            .embed(embeddingTask.getCover(), embeddingTask.getSecret());
                    ImageWriter.write(embedded, embeddingTask.getCover(), embeddingTask.getDirectory());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });

        consumer.start();
        return consumer;
    }

    private void joinThreads(List<Thread> threads) {
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while waiting for thread.", e);
        }
    }

}
