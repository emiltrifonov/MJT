package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventBusImpl implements EventBus {
    private final Map<Class<? extends Event<?>>, Set<Subscriber<? extends Event<?>>>> eventSubscriberMap;
    private final Set<Event<?>> events;

    public EventBusImpl() {
        eventSubscriberMap = new HashMap<>();
        events = new HashSet<>();
    }

    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        checkEventType(eventType);
        checkSubscriber(subscriber);

        eventSubscriberMap.putIfAbsent(eventType, new HashSet<>());
        eventSubscriberMap.get(eventType).add(subscriber);
    }

    @Override
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber) throws MissingSubscriptionException {
        checkEventType(eventType);
        checkSubscriber(subscriber);

        checkForMissingSubscriptionException(eventType, subscriber);

        eventSubscriberMap.get(eventType).remove(subscriber);
    }

    @Override
    public <T extends Event<?>> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null.");
        }

        events.add(event);

        if (eventSubscriberMap.containsKey(event.getClass())) {
            for (Subscriber<?> subscriber : eventSubscriberMap.get(event.getClass())) {

                // Safe to cast subscriber to Subscriber<T> due to subscriber being
                // an element in a Set of Subscriber<Event<?>> and
                // this method specifying that <T extends Event<?>>
                ((Subscriber<T>)subscriber).onEvent(event);

            }
        }


        // alternatively we could avoid the cast to Subscriber<T>
        // if we use the Subscriber raw type instead(???)
        /*if (eventSubscriberMap.containsKey(event.getClass())) {
            for (Subscriber subscriber : eventSubscriberMap.get(event.getClass())) {
                subscriber.onEvent(event);
            }
        }*/
    }

    @Override
    public void clear() {
        eventSubscriberMap.clear();
        events.clear();
    }

    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from, Instant to) {
        if (from.equals(to)) {
            return Collections.emptyList();
        }

        List<Event<?>> eventLogs = new ArrayList<>();

        for (Event<?> event : events) {
            if (event.getClass() == eventType && isEventInTimestamp(event, from, to)) {
                eventLogs.add(event);
            }
        }

        return eventLogs;
    }

    @Override
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        if (eventSubscriberMap.get(eventType) == null) {
            return List.of();
        }
        else {
            return List.copyOf(eventSubscriberMap.get(eventType));
        }
    }

    private <T extends Event<?>> void checkEventType(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null.");
        }
    }

    private <T extends Event<?>> void checkSubscriber(Subscriber<? super T> subscriber) {
        if (subscriber == null) {
            throw new IllegalArgumentException("Event type cannot be null.");
        }
    }

    private boolean isEventInTimestamp(Event<?> event, Instant from, Instant to) {
        return event.getTimestamp().compareTo(from) >= 0 && event.getTimestamp().compareTo(to) < 0;
    }

    private <T extends Event<?>> void checkForMissingSubscriptionException(Class<T> eventType, Subscriber<? super T> subscriber) throws MissingSubscriptionException {
        if (eventSubscriberMap.get(eventType) == null) {
            throw new MissingSubscriptionException("Unexisting subscription.");
        }
        if (!eventSubscriberMap.get(eventType).contains(subscriber)) {
            throw new MissingSubscriptionException("No matching subscriber for event.");
        }
    }
}
