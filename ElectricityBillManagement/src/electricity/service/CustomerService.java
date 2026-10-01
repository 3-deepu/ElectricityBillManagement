package electricity.service;

import electricity.exception.InvalidInputException;
import electricity.exception.InvalidMeterReadingException;
import electricity.model.Customer;
import electricity.util.DataStore;
import electricity.util.IdGenerator;

import java.io.IOException;
import java.util.ArrayList;

public class CustomerService {
    private final ArrayList<Customer> customers;

    public CustomerService() {
        customers = DataStore.loadCustomers();
    }

    public Customer create(String name, String mobile, String address, String username)
            throws InvalidInputException {
        if (name.isBlank()) throw new InvalidInputException("Name cannot be empty.");
        if (!mobile.matches("\\d{10}")) throw new InvalidInputException("Mobile must contain 10 digits.");
        if (address.isBlank()) throw new InvalidInputException("Address cannot be empty.");

        String id = IdGenerator.customerId(customers.size());
        Customer c = new Customer(id, name, mobile, address, username);
        customers.add(c);
        save();
        return c;
    }

    public Customer findById(String id) {
        for (Customer c : customers) {
            if (c.getCustomerId().equalsIgnoreCase(id)) return c;
        }
        return null;
    }

    public Customer findByUsername(String username) {
        for (Customer c : customers) {
            if (c.getUsername().equalsIgnoreCase(username)) return c;
        }
        return null;
    }

    public void update(Customer c, String name, String mobile, String address)
            throws InvalidInputException {
        if (!name.isBlank()) c.setName(name);
        if (!mobile.isBlank()) {
            if (!mobile.matches("\\d{10}")) throw new InvalidInputException("Mobile must contain 10 digits.");
            c.setMobile(mobile);
        }
        if (!address.isBlank()) c.setAddress(address);
        save();
    }

    public double recordMeter(Customer c, double current)
            throws InvalidMeterReadingException {
        if (current < c.getPreviousReading()) {
            throw new InvalidMeterReadingException(
                    "Current reading cannot be less than previous reading (" +
                    c.getPreviousReading() + ").");
        }
        double units = current - c.getPreviousReading();
        if (units <= 0) throw new InvalidMeterReadingException("No new units were consumed.");
        c.setCurrentReading(current);
        save();
        return units;
    }

    public void finalizeReadingAfterBill(Customer c) {
        c.setPreviousReading(c.getCurrentReading());
        c.setCurrentReading(0);
        save();
    }

    public void save() {
        try {
            DataStore.saveCustomers(customers);
        } catch (IOException e) {
            throw new RuntimeException("Could not save customer data.", e);
        }
    }
}
