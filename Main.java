
import java.util.*;

class BoundedBlockingQueue {

    private Queue<Integer> queue;
    private int capacity;

    public BoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    public synchronized void enqueue(int item) throws InterruptedException {

        if (queue.size() == capacity) {
            wait();
        }

        queue.add(item);

        System.out.println(
                Thread.currentThread().getName() + "produced: " + item
        );

        notifyAll();
    }

    public synchronized int deque() throws InterruptedException {

        if (queue.isEmpty()) {
            wait();
        }

        int item = queue.poll();

        System.out.println(
                Thread.currentThread().getName() + "consumed: " + item
        );

        notifyAll();

        return item;
    }
}

class Producer implements Runnable {

    private BoundedBlockingQueue queue;

    public Producer(BoundedBlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                queue.enqueue(i);
                Thread.sleep(500);
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {

    private BoundedBlockingQueue queue;

    public Consumer(BoundedBlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                queue.deque();
                Thread.sleep(500);
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Main {

    public static void main(String[] args) {
        BoundedBlockingQueue queue = new BoundedBlockingQueue(3);

        Thread p1 = new Thread(new Producer(queue), "Producer-1");
        Thread p2 = new Thread(new Producer(queue), "Producer-2");

        Thread c1 = new Thread(new Consumer(queue), "Consumer-1");
        Thread c2 = new Thread(new Consumer(queue), "Consumer-2");

        p1.start();
        p2.start();
        c1.start();
        c2.start();
    }
}
