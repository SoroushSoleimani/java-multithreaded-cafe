package cafe.threads;

import cafe.Main;
import cafe.concurrency.CoffeeMachine;
import cafe.concurrency.Inventory;
import cafe.concurrency.OrderQueue;
import cafe.models.Order;
import java.util.concurrent.atomic.AtomicInteger;

public class Barista implements Runnable {
    private final OrderQueue orderQueue;
    private final Inventory inventory;
    private final CoffeeMachine coffeeMachine;
    private final int baristaId;
    
    private static final AtomicInteger processedCount = new AtomicInteger(0);

    public Barista(OrderQueue orderQueue, Inventory inventory, CoffeeMachine coffeeMachine, int baristaId) {
        this.orderQueue = orderQueue;
        this.inventory = inventory;
        this.coffeeMachine = coffeeMachine;
        this.baristaId = baristaId;
    }

    @Override
    public void run() {
        String name = "Barista-" + baristaId;
        try {
            while (processedCount.get() < 50) { 
                Order order = orderQueue.dequeue();
                
                Main.sendToLogger("PREPARATION_START", name + " started preparing Order ID " + order.getId() + " (" + order.getDrinkType().getName() + ")");

                inventory.withdraw(baristaId, 
                        order.getDrinkType().getCoffeeRequired(), 
                        order.getDrinkType().getMilkRequired(), 
                        order.getDrinkType().getCupsRequired());

                boolean needsMachine = order.getDrinkType().isNeedsCoffeeMachine();
                
                if (needsMachine) {
                    coffeeMachine.use(baristaId); 
                }

                Thread.sleep(order.getPreparationTime());

                if (needsMachine) {
                    coffeeMachine.release(baristaId); 
                }

                Main.sendToLogger("PREPARATION_END", name + " delivered Order ID " + order.getId() + " for customer " + order.getCustomerName());                
                
                processedCount.incrementAndGet();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static int getProcessedCount() {
        return processedCount.get();
    }
}