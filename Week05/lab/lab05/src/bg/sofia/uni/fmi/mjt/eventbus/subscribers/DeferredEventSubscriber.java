package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

import bg.sofia.uni.fmi.mjt.eventbus.comparators.EventByPriorityAndTimestampComparator;
import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

public class DeferredEventSubscriber<T extends Event<?>> implements Subscriber<T>, Iterable<T> {

    private final LinkedList<T> events;

    public DeferredEventSubscriber() {
        events = new LinkedList<>();
    }

    /**
     * Store an event for processing at a later time.
     *
     * @param event the event to be processed
     * @throws IllegalArgumentException if the event is null
     */
    @Override
    public void onEvent(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null.");
        }

        events.add(event);
    }

    private class DeferredEventSubscriberIterator implements Iterator<T> {
        private final PriorityQueue<T> itPq;

        public DeferredEventSubscriberIterator() {
            itPq = new PriorityQueue<>(new EventByPriorityAndTimestampComparator());
            itPq.addAll(events);
        }

        @Override
        public boolean hasNext() {
            return !itPq.isEmpty();
        }

        @Override
        public T next() {
            if (itPq.isEmpty()) {
                throw new NoSuchElementException("Iterating through empty collection.");
            }

            return itPq.poll();
        }
    }

    /**
     * Get an iterator for the unprocessed events. The iterator should provide the events sorted
     * by priority, with higher-priority events first (lower priority number = higher priority).
     * For events with equal priority, earlier events (by timestamp) come first.
     *
     * @return an iterator for the unprocessed events
     */
    @Override
    public Iterator<T> iterator() {
        return new DeferredEventSubscriberIterator();
    }

    /**
     * Check if there are unprocessed events.
     *
     * @return true if there are unprocessed events, false otherwise
     */
    public boolean isEmpty() {
        return events.isEmpty();
    }

}