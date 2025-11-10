package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.DeferredEventSubscriber;

import java.sql.SQLOutput;
import java.util.PriorityQueue;
import java.util.Queue;

public class Main {
    static void main() {
        PriorityQueue<Integer> q = new PriorityQueue<>();
        System.out.println(Integer.compare(1,2));
        q.add(3);
        q.add(4);
        q.add(2);

        for (int i = 0; i < 3; i++) {
            System.out.println(q.peek());
            q.poll();
        }
    }
}
