package cafe.concurrency;

import cafe.Main;

public class Inventory {
    private int coffee = 30; // مقدار اولیه قهوه
    private int milk = 15;   // مقدار اولیه شیر
    private int cups = 30;   // مقدار اولیه لیوان

    public synchronized void withdraw(int baristaId, int reqCoffee, int reqMilk, int reqCups) throws InterruptedException {
        // Wait if not enough ingredients
        while (coffee < reqCoffee || milk < reqMilk || cups < reqCups) {
            wait();
        }
        
        coffee -= reqCoffee;
        milk -= reqMilk;
        cups -= reqCups;
        
        Main.sendToLogger("INVENTORY_WITHDRAW", "Barista-" + baristaId + " withdrew ingredients. (Coffee: " + coffee + ", Milk: " + milk + ", Cups: " + cups + ")");
    }

    public synchronized void refill(int addCoffee, int addMilk, int addCups) {
        coffee += addCoffee;
        milk += addMilk;
        cups += addCups;
        
        Main.sendToLogger("INVENTORY_REFILL", "Inventory refilled. Current status -> Coffee: " + coffee + ", Milk: " + milk + ", Cups: " + cups);
        notifyAll(); // Wake up waiting baristas
    }

    public synchronized String getStatus() {
        return "Coffee: " + coffee + ", Milk: " + milk + ", Cups: " + cups;
    }
}