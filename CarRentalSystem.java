import java.util.*;

class Car {
    private int carId;
    private String model;
    private String type;
    private String registrationNumber;
    private boolean available;

    public Car(int carId, String model, String type, String registrationNumber, boolean available) {
        this.carId = carId;
        this.model = model;
        this.type = type;
        this.registrationNumber = registrationNumber;
        this.available = available;
    }

    public int getCarId() { return carId; }
    public String getModel() { return model; }
    public String getType() { return type; }
    public String getRegistrationNumber() { return registrationNumber; }
    public boolean isAvailable() { return available; }

    public void setModel(String model) { this.model = model; }
    public void setType(String type) { this.type = type; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return carId + " - " + model + " (" + type + ") | Reg: " + registrationNumber +
                " | " + (available ? "Available" : "Rented");
    }
}

class Customer {
    private int customerId;
    private String name;
    private String phone;

    public Customer(int customerId, String name, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
    }

    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }

    @Override
    public String toString() {
        return customerId + " - " + name + " | " + phone;
    }
}

class Booking {
    private int bookingId;
    private Car car;
    private Customer customer;
    private int days;
    private double totalCost;
    private boolean active;

    public Booking(int bookingId, Car car, Customer customer, int days, double totalCost) {
        this.bookingId = bookingId;
        this.car = car;
        this.customer = customer;
        this.days = days;
        this.totalCost = totalCost;
        this.active = true;
    }

    public int getBookingId() { return bookingId; }
    public Car getCar() { return car; }
    public Customer getCustomer() { return customer; }
    public int getDays() { return days; }
    public double getTotalCost() { return totalCost; }
    public boolean isActive() { return active; }

    public void closeBooking() {
        this.active = false;
        this.car.setAvailable(true);
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId + " | Car: " + car.getModel() + " | Customer: " + customer.getName() +
                " | Days: " + days + " | Cost: ₹" + totalCost + " | Status: " + (active ? "Active" : "Closed");
    }
}

public class CarRentalSystem {
    private static List<Car> cars = new ArrayList<>();
    private static List<Customer> customers = new ArrayList<>();
    private static List<Booking> bookings = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);
    private static int bookingCounter = 1;

    public static void main(String[] args) {
        seedData();
        int choice;
        do {
            System.out.println("\n=== Car Rental System ===");
            System.out.println("1. View All Cars");
            System.out.println("2. Add New Car");
            System.out.println("3. Update Car Availability");
            System.out.println("4. Register Customer");
            System.out.println("5. View Customers");
            System.out.println("6. Book a Car");
            System.out.println("7. View All Bookings");
            System.out.println("8. Return Car (End Booking)");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewCars();
                    break;
                case 2:
                    addCar();
                    break;
                case 3:
                    updateCarAvailability();
                    break;
                case 4:
                    registerCustomer();
                    break;
                case 5:
                    viewCustomers();
                    break;
                case 6:
                    createBooking();
                    break;
                case 7:
                    viewBookings();
                    break;
                case 8:
                    closeBooking();
                    break;
                case 9:
                    System.out.println("👋 Thank you for using Car Rental System!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
                    break;
            }
        } while (choice != 9);
    }

    private static void seedData() {
        cars.add(new Car(1, "Toyota Innova", "SUV", "MH12AB1234", true));
        cars.add(new Car(2, "Hyundai i20", "Hatchback", "MH14XY5678", true));
        cars.add(new Car(3, "Honda City", "Sedan", "MH15QW1111", true));

        customers.add(new Customer(1, "Alice", "9876543210"));
        customers.add(new Customer(2, "Bob", "9123456780"));
    }

    private static void viewCars() {
        System.out.println("\n--- Car Inventory ---");
        for (Car c : cars) System.out.println(c);
    }

    private static void addCar() {
        System.out.print("Enter Car ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Model: ");
        String model = sc.nextLine();
        System.out.print("Enter Type (SUV/Sedan/Hatchback): ");
        String type = sc.nextLine();
        System.out.print("Enter Registration Number: ");
        String reg = sc.nextLine();
        cars.add(new Car(id, model, type, reg, true));
        System.out.println("✅ Car added successfully!");
    }

    private static void updateCarAvailability() {
        System.out.print("Enter Car ID to Update: ");
        int id = sc.nextInt();
        Car car = findCar(id);
        if (car == null) {
            System.out.println("❌ Car not found.");
            return;
        }
        car.setAvailable(!car.isAvailable());
        System.out.println("✅ Car availability updated to: " + (car.isAvailable() ? "Available" : "Rented"));
    }

    private static void registerCustomer() {
        System.out.print("Enter Customer ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();
        customers.add(new Customer(id, name, phone));
        System.out.println("✅ Customer registered successfully!");
    }

    private static void viewCustomers() {
        System.out.println("\n--- Registered Customers ---");
        for (Customer c : customers) System.out.println(c);
    }

    private static void createBooking() {
        System.out.print("Enter Car ID: ");
        int carId = sc.nextInt();
        Car car = findCar(carId);
        if (car == null || !car.isAvailable()) {
            System.out.println("❌ Car not available for booking.");
            return;
        }

        System.out.print("Enter Customer ID: ");
        int custId = sc.nextInt();
        Customer cust = findCustomer(custId);
        if (cust == null) {
            System.out.println("❌ Customer not found.");
            return;
        }

        System.out.print("Enter number of rental days: ");
        int days = sc.nextInt();
        double ratePerDay = 1000.0;
        double total = ratePerDay * days;

        Booking booking = new Booking(bookingCounter++, car, cust, days, total);
        bookings.add(booking);
        car.setAvailable(false);
        System.out.println("✅ Booking confirmed! Total cost: ₹" + total);
    }

    private static void viewBookings() {
        System.out.println("\n--- Active & Closed Bookings ---");
        if (bookings.isEmpty()) {
            System.out.println("No bookings yet.");
        } else {
            for (Booking b : bookings) System.out.println(b);
        }
    }

    private static void closeBooking() {
        System.out.print("Enter Booking ID to close: ");
        int id = sc.nextInt();
        for (Booking b : bookings) {
            if (b.getBookingId() == id && b.isActive()) {
                b.closeBooking();
                System.out.println("✅ Booking closed and car returned!");
                return;
            }
        }
        System.out.println("❌ Active booking not found for this ID.");
    }

    private static Car findCar(int id) {
        for (Car c : cars) if (c.getCarId() == id) return c;
        return null;
    }

    private static Customer findCustomer(int id) {
        for (Customer c : customers) if (c.getCustomerId() == id) return c;
        return null;
    }
}
