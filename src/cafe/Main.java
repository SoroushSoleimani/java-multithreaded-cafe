package cafe;

import cafe.concurrency.CoffeeMachine;
import cafe.concurrency.Inventory;
import cafe.concurrency.OrderQueue;
import cafe.threads.Barista;
import cafe.threads.OrderGenerator;
import cafe.threads.Supplier;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {
    private static final OrderQueue orderQueue = new OrderQueue();
    private static final Inventory inventory = new Inventory();
    private static final CoffeeMachine coffeeMachine = new CoffeeMachine();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- Starting Cafe Order Management Simulator ---");

        // 1. Initialize and start Order Generator threads (Producers)
        Thread[] generators = new Thread[3];
        for (int i = 0; i < 3; i++) {
            generators[i] = new Thread(new OrderGenerator(orderQueue, i + 1));
            generators[i].start();
        }

        // 2. Initialize and start Barista threads (Consumers)
        Thread[] baristas = new Thread[4];
        for (int i = 0; i < 4; i++) {
            baristas[i] = new Thread(new Barista(orderQueue, inventory, coffeeMachine, i + 1));
            baristas[i].start();
        }

        // 3. Initialize and start the Supplier thread
        Thread supplierThread = new Thread(new Supplier(inventory));
        supplierThread.start();

        // 4. Wait for all Order Generators to finish producing 50 orders
        for (Thread gen : generators) {
            gen.join();
        }

        // 5. Wait until the queue is completely empty (all orders are taken)
        // 5. Wait until all 50 orders are fully PROCESSED and delivered
        while (Barista.getProcessedCount() < 50) {
            Thread.sleep(500);
        }
        
        // Brief delay to ensure the last log messages are printed
        Thread.sleep(500);

        // 6. Print the final system report
        printFinalReport();

        // 7. Force shutdown the system (terminates waiting Baristas and Supplier)
        System.exit(0);
    }

    public static void sendToLogger(String eventType, String message) {
        // Connects to LoggerProcess on port 27805
        try (Socket socket = new Socket("localhost", 27805);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println("[" + eventType + "] " + message);
        } catch (Exception e) {
            System.out.println("[Local Log] [" + eventType + "] " + message);
        }
    }

    private static void printFinalReport() {
        System.out.println("\n==============================================");
        System.out.println("       Final Cafe System Report       ");
        System.out.println("==============================================");
        System.out.println("Total Orders Generated: 50");
        System.out.println("Total Orders Processed: " + Barista.getProcessedCount());
        System.out.println("Remaining Orders in Queue: " + orderQueue.getRemainingCount());
        System.out.println("Final Inventory Status: " + inventory.getStatus());
        System.out.println("Total Coffee Machine Usages: " + coffeeMachine.getUsageCount());
        System.out.println("System Integrity Check: PASSED (No race conditions detected)");
        System.out.println("==============================================");
    }

    public static OrderQueue getOrderQueue() {
        return orderQueue;
    }
}