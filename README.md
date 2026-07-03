# Cafe Order Management Simulator

A concurrent and distributed cafe order management simulator developed as part of the Operating Systems course project under the supervision of **Dr. Khanmirza** at **K. N. Toosi University of Technology**.

The system simulates a real-world cafe environment using multi-threading, concurrency control mechanisms, and Inter-Process Communication (IPC).

## Features & Architecture

### 1. Core Simulation (Main Process)
- **Order Generation (Producers):** 3 dedicated threads dynamically create random orders (up to 50 total orders) with unique IDs, customer names, drink types, preparation times, and priorities.
- **Baristas (Consumers):** 4 parallel barista threads process incoming orders from a shared queue, withdraw required ingredients, and coordinate device usages.
- **Bounded Buffer Queue:** A shared order queue with a strict capacity limit of 10. Thread synchronization is handled via `Semaphores` (to track available empty spaces and items) and a `ReentrantLock` (Mutex) to guard the critical section.
- **Limited Resources Control:** Access to the limited coffee machines (maximum 2 concurrent users) is securely managed using a `Semaphore(2)`.
- **Inventory Management (Monitor Structure):** Built using Java's implicit monitor synchronization mechanism (`synchronized`, `wait()`, and `notifyAll()`). Baristas wait if ingredients are low, and a dedicated **Supplier thread** automatically refills the stock safely.

### 2. Advanced Logging System (Bonus Section - IPC via Sockets)
- A decoupled, standalone process (`LoggerProcess`) acts as a localized log server.
- The main simulation communicates with the Logger process using **Network Sockets** as an Inter-Process Communication (IPC) mechanism.
- Every system event is captured synchronously, displayed elegantly in the server terminal, and persistently appended to `cafe_system.log`.

---

## Project Structure

```text
cafe_simulation/
│
├── src/
│   └── cafe/
│       ├── concurrency/     # OrderQueue, Inventory, CoffeeMachine (Monitors & Semaphores)
│       ├── logger/          # LoggerProcess (Independent IPC Logger Server)
│       ├── models/          # Order, DrinkType
│       ├── threads/         # Barista, OrderGenerator, Supplier
│       └── Main.java        # Core Application Entry Point
│
├── cafe_system.log          # Persisted output log file
└── README.md                # Project documentation
```

## How to Run the Project

Follow these steps exactly in your terminal to execute the simulation smoothly:

### Step 1: Compile the Project
Open a command prompt or terminal in the project's root directory (`cafe_simulation`) and run the following command to compile all source files:

```bash
javac src\cafe\models\*.java src\cafe\concurrency\*.java src\cafe\threads\*.java src\cafe\logger\*.java src\cafe\*.java
```

### Step 2: Start the Logger Process (Server)
Launch the standalone logger process in your first terminal window. This process will spin up and listen on port 27805:

```bash
java -cp src cafe.logger.LoggerProcess
```

Expected output: `[Logger Process] Listening on port 27805...`

### Step 3: Run the Cafe Simulation (Client)
Without closing the first terminal, open a second terminal tab or window (`+` in VS Code Terminal) in the same directory and execute the main simulation:

```bash
java -cp src cafe.Main
```

## Expected Output

Once `Main` is executed, the Logger terminal will stream real-time concurrency events detailing order creation, enqueueing/dequeueing, ingredient withdrawals, coffee machine locks, and deliveries.

At the end of the simulation, the system automatically shuts down gracefully and prints a clean final synchronization report:

```text
--- Starting Cafe Order Management Simulator ---

==============================================
       Final Cafe System Report       
==============================================
Total Orders Generated: 50
Total Orders Processed: 50
Remaining Orders in Queue: 0
Final Inventory Status: Coffee: 64, Milk: 0, Cups: 100
Total Coffee Machine Usages: 43
System Integrity Check: PASSED (No race conditions detected)
==============================================
```

All recorded outputs will also be fully saved inside the `cafe_system.log` file.
