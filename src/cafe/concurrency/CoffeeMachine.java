package cafe.concurrency;

import cafe.Main;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class CoffeeMachine {
    // Semaphore to limit access to 2 baristas at a time
    private final Semaphore machineSemaphore = new Semaphore(2);
    private final AtomicInteger usageCount = new AtomicInteger(0);

    public void use(int baristaId) throws InterruptedException {
        machineSemaphore.acquire();
        usageCount.incrementAndGet();
        Main.sendToLogger("COFFEE_MACHINE_USE", "Barista-" + baristaId + " is using the coffee machine.");
    }

    public void release(int baristaId) {
        Main.sendToLogger("COFFEE_MACHINE_RELEASE", "Barista-" + baristaId + " finished using the coffee machine.");
        machineSemaphore.release();
    }

    public int getUsageCount() {
        return usageCount.get();
    }
}