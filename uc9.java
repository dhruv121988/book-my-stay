
import java.util.*;

// ---------------- CUSTOM EXCEPTION ----------------
class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}

// ---------------- BOOKING SYSTEM ----------------
class BookingSystem {

    private Map<String, Integer> inventory = new HashMap<>();

    public BookingSystem() {
        inventory.put("Single", 2);
        inventory.put("Double", 1);
    }

    // Validation + Booking
    public void bookRoom(String roomType)
            throws InvalidBookingException {

        // ---- VALIDATE ROOM TYPE ----
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException(
                    "Invalid Room Type: " + roomType);
        }

        // ---- CHECK AVAILABILITY ----
        int available = inventory.get(roomType);

        if (available <= 0) {
            throw new InvalidBookingException(
                    "No rooms available for " + roomType);
        }

        // ---- SAFE STATE UPDATE ----
        inventory.put(roomType, available - 1);

        System.out.println("Booking successful for: " + roomType);
    }

    public void showInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// ---------------- MAIN CLASS ----------------
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        try {
            system.bookRoom("Single");   // valid
            system.bookRoom("Suite");    // invalid type
            system.bookRoom("Double");   // valid
            system.bookRoom("Double");   // no availability

        } catch (InvalidBookingException e) {

            // Graceful failure handling
            System.out.println("Booking Failed: " + e.getMessage());
        }

        // System continues safely
        system.showInventory();
    }
}
