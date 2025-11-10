package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventBusImpl implements EventBus {
    // Class<?> -> Event<?> maybe?????
    // *********** or maybe nvm Class<T> means smth else
    Map<Class<?>, Set<Subscriber<?>>> eventSubscriberMap;
    Set<Event<?>> events;

    public EventBusImpl() {
        eventSubscriberMap = new HashMap<>();
        events = new HashSet<>();
    }

    // parameter T extends Event<?> so should be
    // safe to cast Class<T> to Event<?> maybe?? or smth else
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

        if (eventSubscriberMap.get(eventType) == null) {
            throw new MissingSubscriptionException("Unexisting subscription.");
        }
        if (!eventSubscriberMap.get(eventType).contains(subscriber)) {
            throw new MissingSubscriptionException("No matching subscriber for event.");
        }

        eventSubscriberMap.get(eventType).remove(subscriber);
    }

    @Override
    public <T extends Event<?>> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null.");
        }

        events.add(event);

        for (Class<?> eventType : eventSubscriberMap.keySet()) {
            if (event.getClass() == eventType) {
                for (Subscriber<?> subscriber : eventSubscriberMap.get(eventType)) {
                    //subscriber.onEvent(event); gg
                }
            }
        }
    }

    @Override
    public void clear() {
        eventSubscriberMap.clear();
        events.clear();
    }

    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from, Instant to) {
        Collection<Event<?>> eventLogs = new HashSet<>();

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
            return Set.of();
        }
        else {
            return Set.copyOf(eventSubscriberMap.get(eventType));
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
        return event.getTimestamp().compareTo(from) >= 0 && event.getTimestamp().compareTo(to) <= 0;
    }
}
