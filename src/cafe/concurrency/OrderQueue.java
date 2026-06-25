package cafe.concurrency;

import cafe.Main;
import cafe.models.Order;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class OrderQueue {
    private final Queue<Order> queue = new LinkedList<>();
    private final int capacity = 10;
    
    // Semaphores for queue state management
    private final Semaphore spaces = new Semaphore(capacity);
    private final Semaphore items = new Semaphore(0);
    
    // Mutex for critical section protection
    private final ReentrantLock lock = new ReentrantLock();

    public void enqueue(Order newOrder) throws InterruptedException {
        spaces.acquire(); // Blocks if queue is full
        
        lock.lock();      // Enter critical section
        try {
            queue.add(newOrder);
            Main.sendToLogger("ORDER_ENQUEUED", "Order ID " + newOrder.getId() + " enqueued. Current queue size: " + queue.size());
        } finally {
            lock.unlock(); // Exit critical section
        }
        
        items.release(); // Signal consumers
    }

    public Order dequeue() throws InterruptedException {
        items.acquire(); // Blocks if queue is empty
        
        Order order;
        lock.lock();     // Enter critical section
        try {
            order = queue.poll();
            Main.sendToLogger("ORDER_DEQUEUED", "Order ID " + order.getId() + " dequeued by Barista.");
        } finally {
            lock.unlock(); // Exit critical section
        }
        
        spaces.release(); // Signal producers
        return order;
    }

    public int getRemainingCount() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
}