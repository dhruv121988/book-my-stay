import java.util.*;

// ---------------- ADD-ON SERVICE ----------------
class AddOnService {

    private String serviceName;
    private double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + price + ")";
    }
}

// ---------------- SERVICE MANAGER ----------------
class AddOnServiceManager {

    // ReservationID -> List of Services
    private Map<String, List<AddOnService>> reservationServices =
            new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId,
                           AddOnService service) {

        reservationServices
                .computeIfAbsent(reservationId,
                        k -> new ArrayList<>())
                .add(service);

        System.out.println(service.getServiceName()
                + " added to Reservation " + reservationId);
    }

    // Display services
    public void showServices(String reservationId) {

        List<AddOnService> services =
                reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Services for Reservation "
                + reservationId + ":");

        for (AddOnService s : services) {
            System.out.println(" - " + s);
        }
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {

        List<AddOnService> services =
                reservationServices.get(reservationId);

        if (services == null) return 0;

        double total = 0;

        for (AddOnService s : services) {
            total += s.getPrice();
        }

        return total;
    }
}

// ---------------- MAIN CLASS ----------------
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        AddOnServiceManager manager =
                new AddOnServiceManager();

        String reservationId = "RES101";

        // Guest selects add-on services
        manager.addService(reservationId,
                new AddOnService("Breakfast", 500));

        manager.addService(reservationId,
                new AddOnService("Airport Pickup", 1200));

        manager.addService(reservationId,
                new AddOnService("Extra Bed", 800));

        // Show selected services
        manager.showServices(reservationId);

        // Calculate total cost
        double total =
                manager.calculateTotalCost(reservationId);

        System.out.println("Total Add-On Cost: ₹" + total);
    }
}