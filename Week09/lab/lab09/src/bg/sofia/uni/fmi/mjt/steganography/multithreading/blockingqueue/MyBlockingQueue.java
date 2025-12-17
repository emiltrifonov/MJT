package bg.sofia.uni.fmi.mjt.steganography.multithreading.blockingqueue;

import java.util.LinkedList;

public class MyBlockingQueue<T> {

    private final LinkedList<T> queue = new LinkedList<>();

    public synchronized void put(T el) {
        queue.addLast(el);
        notifyAll();
    }

    public synchronized T get() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }

        return queue.removeFirst();
    }

}
