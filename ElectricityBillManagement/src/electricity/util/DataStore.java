package electricity.util;

import electricity.model.Bill;
import electricity.model.Customer;
import electricity.model.User;

import java.io.*;
import java.nio.file.*;

import java.util.ArrayList;

public final class DataStore {
    private static final Path DATA_DIR = Paths.get("data");
    private static final Path USERS = DATA_DIR.resolve("users.dat");
    private static final Path CUSTOMERS = DATA_DIR.resolve("customers.dat");
    private static final Path BILLS = DATA_DIR.resolve("bills.dat");

    private DataStore() {}

    public static void initialize() {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create data directory.", e);
        }
    }

    public static void saveUsers(ArrayList<User> list) throws IOException {
        save(USERS, list);
    }

    public static void saveCustomers(ArrayList<Customer> list) throws IOException {
        save(CUSTOMERS, list);
    }

    public static void saveBills(ArrayList<Bill> list) throws IOException {
        save(BILLS, list);
    }

    public static ArrayList<User> loadUsers() {
        return load(USERS);
    }

    public static ArrayList<Customer> loadCustomers() {
        return load(CUSTOMERS);
    }

    public static ArrayList<Bill> loadBills() {
        return load(BILLS);
    }

    private static <T> void save(Path path, ArrayList<T> list) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(path))) {
            out.writeObject(list);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> ArrayList<T> load(Path path) {
        if (!Files.exists(path)) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
            return (ArrayList<T>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Warning: Could not load " + path + ". Starting with empty data.");
            return new ArrayList<>();
        }
    }
}
