/**
 * Book My Stay App
 * Use Case 12: Data Persistence & System Recovery
 *
 * Demonstrates serialization and deserialization
 * to persist inventory and booking data across restarts.
 *
 * @author Adarsh
 * @version 12.1
 */

import java.io.*;
import java.util.*;


/* ---------------- RESERVATION MODEL ---------------- */

class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String toString() {
        return guestName + " | " + roomType + " | " + roomId;
    }
}


/* ---------------- INVENTORY ---------------- */

class RoomInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    private HashMap<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public void reduce(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public HashMap<String, Integer> getInventory() {
        return inventory;
    }

    public void display() {
        System.out.println("\nInventory State:");
        inventory.forEach((k, v) ->
                System.out.println(k + " : " + v));
    }
}


/* ---------------- SYSTEM STATE (SNAPSHOT) ---------------- */

class SystemState implements Serializable {

    private static final long serialVersionUID = 1L;

    RoomInventory inventory;
    List<Reservation> bookings;

    public SystemState(RoomInventory inventory,
                       List<Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}


/* ---------------- PERSISTENCE SERVICE ---------------- */

class PersistenceService {

    private static final String FILE_NAME = "hotel_state.dat";

    // SAVE STATE
    public static void save(SystemState state) {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);
            System.out.println("\n✅ System state saved successfully.");

        } catch (IOException e) {
            System.out.println("❌ Error saving system state.");
        }
    }

    // LOAD STATE
    public static SystemState load() {

        File file = new File(FILE_NAME);

        // Handle missing file safely
        if (!file.exists()) {
            System.out.println("\n⚠ No previous state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) in.readObject();
            System.out.println("\n✅ System state restored successfully.");
            return state;

        } catch (Exception e) {
            System.out.println("❌ Corrupted state file. Starting fresh.");
            return null;
        }
    }
}


/* ---------------- APPLICATION ENTRY ---------------- */

public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("      Book My Stay App v12.1");
        System.out.println("   Data Persistence & Recovery");
        System.out.println("========================================");

        // Attempt recovery
        SystemState recoveredState = PersistenceService.load();

        RoomInventory inventory;
        List<Reservation> bookings;

        if (recoveredState != null) {
            inventory = recoveredState.inventory;
            bookings = recoveredState.bookings;
        } else {
            inventory = new RoomInventory();
            bookings = new ArrayList<>();
        }

        // Simulate new booking
        String roomId = "SR-" + new Random().nextInt(1000);
        Reservation r =
                new Reservation("Adarsh", "Single Room", roomId);

        bookings.add(r);
        inventory.reduce("Single Room");

        System.out.println("\nNew Booking Added:");
        System.out.println(r);

        // Display current state
        inventory.display();

        System.out.println("\nBooking History:");
        bookings.forEach(System.out::println);

        // Save state before shutdown
        SystemState currentState =
                new SystemState(inventory, bookings);

        PersistenceService.save(currentState);

        System.out.println("\nSystem shutdown complete.");
    }
}