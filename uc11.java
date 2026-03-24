/**
 * Book My Stay App
 * Use Case 11: Concurrent Booking Simulation
 *
 * Demonstrates thread safety using synchronized
 * access to shared booking queue and inventory.
 *
 * @author Adarsh
 * @version 11.1
 */

import java.util.*;


/* ---------------- RESERVATION ---------------- */

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}


/* ---------------- INVENTORY (SHARED RESOURCE) ---------------- */

class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    // CRITICAL SECTION (THREAD SAFE)
    public synchronized boolean allocateRoom(String roomType) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);

            String roomId =
                    roomType.substring(0, 2).toUpperCase()
                            + "-" + new Random().nextInt(1000);

            System.out.println(Thread.currentThread().getName()
                    + " ✅ Allocated " + roomId
                    + " to " + roomType);

            return true;
        }

        System.out.println(Thread.currentThread().getName()
                + " ❌ No rooms available for " + roomType);

        return false;
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory State:");
        inventory.forEach((k, v) ->
                System.out.println(k + " : " + v));
    }
}


/* ---------------- BOOKING QUEUE (SHARED) ---------------- */

class BookingQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    // synchronized enqueue
    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
        System.out.println("Request Added → "
                + r.getGuestName() + " (" + r.getRoomType() + ")");
    }

    // synchronized dequeue
    public synchronized Reservation getNextRequest() {
        return queue.poll();
    }
}


/* ---------------- BOOKING PROCESSOR THREAD ---------------- */

class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(String name,
                            BookingQueue queue,
                            RoomInventory inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {

            Reservation r = queue.getNextRequest();

            if (r == null)
                break;

            // simulate processing delay
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}

            inventory.allocateRoom(r.getRoomType());
        }
    }
}


/* ---------------- MAIN APPLICATION ---------------- */

public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args)
            throws InterruptedException {

        System.out.println("=======================================");
        System.out.println(" Book My Stay App v11.1");
        System.out.println(" Concurrent Booking Simulation");
        System.out.println("=======================================");

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Multiple guests booking simultaneously
        queue.addRequest(new Reservation("Adarsh", "Single Room"));
        queue.addRequest(new Reservation("Rahul", "Single Room"));
        queue.addRequest(new Reservation("Priya", "Single Room"));
        queue.addRequest(new Reservation("Amit", "Double Room"));
        queue.addRequest(new Reservation("Neha", "Suite Room"));

        // Multiple processing threads
        BookingProcessor t1 =
                new BookingProcessor("Processor-1", queue, inventory);

        BookingProcessor t2 =
                new BookingProcessor("Processor-2", queue, inventory);

        BookingProcessor t3 =
                new BookingProcessor("Processor-3", queue, inventory);

        // Start concurrent execution
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        t1.join();
        t2.join();
        t3.join();

        // Final inventory state
        inventory.displayInventory();

        System.out.println("\nAll bookings processed safely.");
    }
}