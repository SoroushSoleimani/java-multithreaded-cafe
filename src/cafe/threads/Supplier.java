package cafe.threads;

import cafe.concurrency.Inventory;

public class Supplier implements Runnable {
    private final Inventory inventory;

    public Supplier(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(15000); 
                inventory.refill(20, 10, 20);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}