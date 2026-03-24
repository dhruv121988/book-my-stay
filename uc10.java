import java.util.*;

class BookingSystem {

    private Map<String, Integer> inventory = new HashMap<>();
    private Map<Integer, String> bookings = new HashMap<>();
    private Stack<Integer> rollbackStack = new Stack<>();

    private int bookingIdCounter = 1;

    public BookingSystem() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
    }

    // Booking
    public void bookRoom(String type) {
        if (inventory.getOrDefault(type, 0) > 0) {
            int bookingId = bookingIdCounter++;
            bookings.put(bookingId, type);

            inventory.put(type, inventory.get(type) - 1);

            System.out.println("Booking Confirmed ID: " + bookingId);
        } else {
            System.out.println("No rooms available.");
        }
    }

    // Cancellation with rollback
    public void cancelBooking(int bookingId) {

        if (!bookings.containsKey(bookingId)) {
            System.out.println("Invalid Booking ID");
            return;
        }

        String roomType = bookings.remove(bookingId);

        // push into rollback stack
        rollbackStack.push(bookingId);

        // restore inventory
        inventory.put(roomType, inventory.get(roomType) + 1);

        System.out.println("Booking Cancelled: " + bookingId);
    }

    public void showInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        system.bookRoom("Single");
        system.bookRoom("Double");

        system.showInventory();

        system.cancelBooking(1);

        system.showInventory();
    }
}