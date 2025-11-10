package bg.sofia.uni.fmi.mjt.eventbus.comparators;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

import java.util.Comparator;

public class EventByPriorityAndTimestampComparator implements Comparator<Event<?>> {
    public int compare(Event<?> e1, Event<?> e2) {
        if (e1.getPriority() == e2.getPriority()) {
            return e1.getTimestamp().compareTo(e2.getTimestamp());
        }
        return Integer.compare(e1.getPriority(), e2.getPriority());
    }
}
