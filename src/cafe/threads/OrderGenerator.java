package cafe.threads;

import cafe.Main;
import cafe.concurrency.OrderQueue;
import cafe.models.DrinkType;
import cafe.models.Order;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderGenerator implements Runnable {
    private static final AtomicInteger totalOrdersCreated = new AtomicInteger(0);
    private static final int MAX_ORDERS = 50;
    
    private final OrderQueue orderQueue;
    private final int generatorId;
    private final Random random = new Random();

    public OrderGenerator(OrderQueue orderQueue, int generatorId) {
        this.orderQueue = orderQueue;
        this.generatorId = generatorId;
    }

    @Override
    public void run() {
        String[] customers = {"Ali", "Sana", "Reza", "Mina", "Soroush", "Zahra", "Amir"};
        
        while (true) {
            int orderId = totalOrdersCreated.incrementAndGet();
            if (orderId > MAX_ORDERS) {
                break;
            }

            String customer = customers[random.nextInt(customers.length)];
            DrinkType type = DrinkType.values()[random.nextInt(DrinkType.values().length)];
            int prepTime = 1000 + random.nextInt(2000); 
            int priority = random.nextInt(5);

            Order newOrder = new Order(orderId, customer, type, prepTime, priority);
            Main.sendToLogger("ORDER_CREATED", "OrderGenerator-" + generatorId + " created new order: " + customer + " (" + type + ")");
            try {
                orderQueue.enqueue(newOrder); 
                Thread.sleep(500 + random.nextInt(1000)); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}