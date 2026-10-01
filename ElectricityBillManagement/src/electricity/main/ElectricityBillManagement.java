package electricity.main;

import electricity.exception.*;
import electricity.model.*;
import electricity.service.*;
import electricity.util.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ElectricityBillManagement {
    private final AuthService authService = new AuthService();
    private final CustomerService customerService = new CustomerService();
    private final BillService billService = new BillService();
    private final PaymentService paymentService = new PaymentService();

    private User currentUser;
    private Customer currentCustomer;

    public static void main(String[] args) {
        DataStore.initialize();

        // AWT/Swing is used for a short graphical startup animation.
        if (!GraphicsEnvironment.isHeadless()) {
            GuiSplash.showSplash();
        }

        new ElectricityBillManagement().run();
    }

    private void run() {
        welcome();
        boolean running = true;

        while (running) {
            Colors.title("ELECTRICITY BILL MANAGEMENT SYSTEM");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");

            int choice = InputUtil.readChoice("Enter choice: ", 1, 3);

            switch (choice) {
                case 1 -> login();
                case 2 -> signUp();
                case 3 -> {
                    Animation.loading("Saving application data", 1);
                    Colors.success("Thank you for using the Electricity Bill Management System.");
                    running = false;
                }
            }
        }
    }

    private void welcome() {
        Colors.title("POWERING YOUR HOME - SMART BILLING");
        Animation.typeLine("Welcome to your secure electricity billing console.", 15);
        Animation.loading("Initializing Core Java services", 1);
        System.out.println();
    }

    private void signUp() {
        Colors.title("SIGN UP");

        try {
            String username = InputUtil.readRequired("Username: ");
            String password = InputUtil.readRequired("Password: ");
            String name = InputUtil.readRequired("Full name: ");
            String mobile = InputUtil.readMobile();
            String address = InputUtil.readRequired("Address: ");

            String customerId = authService.generateCustomerId();
            Customer customer = customerService.create(name, mobile, address, username);
            // Keep generated IDs synchronized with the customer record.
            if (!customer.getCustomerId().equals(customerId)) {
                Colors.error("Internal ID synchronization error.");
                return;
            }

            authService.register(username, password, customer.getCustomerId());

            Animation.loading("Creating account", 1);
            Colors.success("Account created successfully.");
            System.out.println("Customer ID : " + customer.getCustomerId());
            System.out.println("Please use your username and password to log in.");
        } catch (InvalidInputException e) {
            Colors.error(e.getMessage());
        } finally {
            InputUtil.pause();
        }
    }

    private void login() {
        Colors.title("LOGIN");

        try {
            String username = InputUtil.readRequired("Username: ");
            String password = InputUtil.readRequired("Password: ");

            currentUser = authService.login(username, password);
            currentCustomer = customerService.findById(currentUser.getCustomerId());

            if (currentCustomer == null) {
                throw new InvalidInputException("Customer profile not found.");
            }

            Animation.loading("Authenticating", 1);
            Colors.success("Login successful. Welcome, " + currentCustomer.getName() + "!");
            dashboard();
        } catch (InvalidInputException e) {
            Colors.error(e.getMessage());
            InputUtil.pause();
        }
    }

    private void dashboard() {
        boolean loggedIn = true;

        while (loggedIn) {
            Colors.title("MAIN MENU - " + currentCustomer.getName());
            System.out.println("1. Customer Details");
            System.out.println("2. Record Meter Reading");
            System.out.println("3. Generate New Bill");
            System.out.println("4. View Latest Bill");
            System.out.println("5. Pay Latest Unpaid Bill");
            System.out.println("6. Bill History");
            System.out.println("7. Update Customer");
            System.out.println("8. Search Customer");
            System.out.println("9. Logout");

            int choice = InputUtil.readChoice("Enter choice: ", 1, 9);

            switch (choice) {
                case 1 -> customerDetails();
                case 2 -> recordMeter();
                case 3 -> generateBill();
                case 4 -> viewLatestBill();
                case 5 -> payBill();
                case 6 -> billHistory();
                case 7 -> updateCustomer();
                case 8 -> searchCustomer();
                case 9 -> {
                    Animation.dots("Logging out", 3);
                    currentUser = null;
                    currentCustomer = null;
                    Colors.success("Logged out successfully.");
                    loggedIn = false;
                }
            }
        }
    }

    private void customerDetails() {
        Colors.title("CUSTOMER DETAILS");
        Colors.line();
        System.out.println("Customer ID       : " + currentCustomer.getCustomerId());
        System.out.println("Name              : " + currentCustomer.getName());
        System.out.println("Mobile            : " + currentCustomer.getMobile());
        System.out.println("Address           : " + currentCustomer.getAddress());
        System.out.println("Username          : " + currentCustomer.getUsername());
        System.out.println("Previous Reading  : " + currentCustomer.getPreviousReading());
        Colors.line();
        InputUtil.pause();
    }

    private void recordMeter() {
        Colors.title("METER READING");

        try {
            System.out.println("Previous reading: " + currentCustomer.getPreviousReading());
            double current = InputUtil.readDouble("Current meter reading: ");

            double units = customerService.recordMeter(currentCustomer, current);

            Animation.loading("Updating smart meter", 1);
            Colors.success("Meter reading recorded.");
            System.out.println("Units consumed: " + units);
        } catch (InvalidMeterReadingException e) {
            Colors.error(e.getMessage());
        } finally {
            InputUtil.pause();
        }
    }

    private void generateBill() {
        Colors.title("GENERATE BILL");

        double previous = currentCustomer.getPreviousReading();
        double current = currentCustomer.getCurrentReading();

        if (current <= previous) {
            Colors.error("Record a new meter reading before generating a bill.");
            InputUtil.pause();
            return;
        }

        double units = current - previous;
        Animation.loading("Calculating slab-based tariff", 1);

        Bill bill = billService.generate(currentCustomer, units);
        customerService.finalizeReadingAfterBill(currentCustomer);

        displayBill(bill);
        Colors.success("Bill generated and saved.");
        InputUtil.pause();
    }

    private void viewLatestBill() {
        Colors.title("LATEST BILL");
        try {
            Bill bill = billService.latestForCustomer(currentCustomer.getCustomerId());
            displayBill(bill);
        } catch (BillNotFoundException e) {
            Colors.error(e.getMessage());
        }
        InputUtil.pause();
    }

    private void payBill() {
        Colors.title("PAYMENT");

        try {
            Bill bill = billService.latestUnpaid(currentCustomer.getCustomerId());
            displayBill(bill);

            System.out.println();
            System.out.println("1. UPI");
            System.out.println("2. Card");
            System.out.println("3. Cash");

            int mode = InputUtil.readChoice("Payment mode: ", 1, 3);
            String credential = "";

            if (mode == 1) {
                credential = InputUtil.readRequired("UPI ID: ");
            } else if (mode == 2) {
                credential = InputUtil.readRequired("16-digit card number: ");
            }

            PaymentReceipt receipt = paymentService.pay(bill, mode, credential);
            billService.save();

            Colors.success("Payment successful.");
            System.out.println("Transaction ID : " + receipt.getTransactionId());
            System.out.println("Bill ID        : " + receipt.getBillId());
            System.out.printf("Amount         : Rs. %.2f%n", receipt.getAmount());
            System.out.println("Payment Mode   : " + receipt.getMode());
            System.out.println("Time           : " + receipt.getTime());

        } catch (BillNotFoundException | PaymentException e) {
            Colors.error(e.getMessage());
        } finally {
            InputUtil.pause();
        }
    }

    private void billHistory() {
        Colors.title("BILL HISTORY");

        List<Bill> history = billService.history(currentCustomer.getCustomerId());

        if (history.isEmpty()) {
            Colors.info("No bills found.");
        } else {
            System.out.printf("%-13s %-12s %-12s %-14s %-10s %-12s%n",
                    "BILL ID", "DATE", "UNITS", "TOTAL", "STATUS", "MODE");
            Colors.line();

            for (Bill b : history) {
                System.out.printf("%-13s %-12s %-12.2f Rs.%-10.2f %-10s %-12s%n",
                        b.getBillId(),
                        b.getBillDate(),
                        b.getUnits(),
                        b.getTotal(),
                        b.getStatus(),
                        b.getPaymentMode());
            }
        }

        InputUtil.pause();
    }

    private void updateCustomer() {
        Colors.title("UPDATE CUSTOMER");

        try {
            System.out.println("Press ENTER to keep an existing value.");
            String name = InputUtil.readLine("New name [" + currentCustomer.getName() + "]: ");
            String mobile = InputUtil.readLine("New mobile [" + currentCustomer.getMobile() + "]: ");
            String address = InputUtil.readLine("New address [" + currentCustomer.getAddress() + "]: ");

            customerService.update(currentCustomer, name, mobile, address);
            Colors.success("Customer details updated.");
        } catch (InvalidInputException e) {
            Colors.error(e.getMessage());
        }

        InputUtil.pause();
    }

    private void searchCustomer() {
        Colors.title("SEARCH CUSTOMER");

        String id = InputUtil.readRequired("Customer ID: ");
        Customer customer = customerService.findById(id);

        if (customer == null) {
            Colors.error("Customer not found.");
        } else {
            Colors.line();
            System.out.println("Customer ID : " + customer.getCustomerId());
            System.out.println("Name        : " + customer.getName());
            System.out.println("Mobile      : " + customer.getMobile());
            System.out.println("Address     : " + customer.getAddress());
            Colors.line();
        }

        InputUtil.pause();
    }

    private void displayBill(Bill bill) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        System.out.println();
        System.out.println("+----------------------------------------------------------+");
        System.out.println("|                 ELECTRICITY BILL                         |");
        System.out.println("+----------------------------------------------------------+");
        System.out.printf("| Bill ID       : %-40s |%n", bill.getBillId());
        System.out.printf("| Customer ID   : %-40s |%n", bill.getCustomerId());
        System.out.printf("| Bill Date     : %-40s |%n", bill.getBillDate().format(fmt));
        System.out.printf("| Due Date      : %-40s |%n", bill.getDueDate().format(fmt));
        System.out.println("+----------------------------------------------------------+");
        System.out.printf("| Units         : %-40.2f |%n", bill.getUnits());
        System.out.printf("| Energy Charge : Rs. %-35.2f |%n", bill.getEnergyCharge());
        System.out.printf("| Fixed Charge  : Rs. %-35.2f |%n", bill.getFixedCharge());
        System.out.printf("| Late Fee      : Rs. %-35.2f |%n", bill.getLateFee());
        System.out.println("+----------------------------------------------------------+");
        System.out.printf("| TOTAL         : Rs. %-35.2f |%n", bill.getTotal());
        System.out.printf("| STATUS        : %-40s |%n", bill.getStatus());
        System.out.printf("| PAYMENT MODE  : %-40s |%n", bill.getPaymentMode());
        System.out.println("+----------------------------------------------------------+");
    }
}
