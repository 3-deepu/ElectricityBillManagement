package electricity.service;

import electricity.exception.BillNotFoundException;
import electricity.model.Bill;
import electricity.model.Customer;
import electricity.util.DataStore;
import electricity.util.IdGenerator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillService {
    private final ArrayList<Bill> bills;

    public BillService() {
        bills = DataStore.loadBills();
    }

    public Bill generate(Customer customer, double units) {
        double energy = calculateEnergyCharge(units);
        double fixed = calculateFixedCharge(units);

        LocalDate billDate = LocalDate.now();
        LocalDate dueDate = billDate.plusDays(15);

        Bill bill = new Bill(
                IdGenerator.billId(bills.size()),
                customer.getCustomerId(),
                billDate,
                dueDate,
                units,
                energy,
                fixed,
                0.0,
                energy + fixed
        );

        bills.add(bill);
        save();
        return bill;
    }

    public double calculateEnergyCharge(double units) {
        double charge;
        if (units <= 100) {
            charge = units * 2.00;
        } else if (units <= 200) {
            charge = 100 * 2.00 + (units - 100) * 3.00;
        } else if (units <= 400) {
            charge = 100 * 2.00 + 100 * 3.00 + (units - 200) * 5.00;
        } else {
            charge = 100 * 2.00 + 100 * 3.00 + 200 * 5.00 + (units - 400) * 7.00;
        }
        return charge;
    }

    public double calculateFixedCharge(double units) {
        if (units <= 100) return 50;
        if (units <= 200) return 100;
        if (units <= 400) return 150;
        return 200;
    }

    public Bill latestForCustomer(String customerId) throws BillNotFoundException {
        Bill latest = null;
        for (Bill bill : bills) {
            if (bill.getCustomerId().equals(customerId)) latest = bill;
        }
        if (latest == null) throw new BillNotFoundException("No bill found for this customer.");
        return latest;
    }

    public Bill latestUnpaid(String customerId) throws BillNotFoundException {
        Bill latest = null;
        for (Bill bill : bills) {
            if (bill.getCustomerId().equals(customerId) && "UNPAID".equals(bill.getStatus())) {
                latest = bill;
            }
        }
        if (latest == null) throw new BillNotFoundException("No unpaid bill found.");
        return latest;
    }

    public List<Bill> history(String customerId) {
        ArrayList<Bill> result = new ArrayList<>();
        for (Bill bill : bills) {
            if (bill.getCustomerId().equals(customerId)) result.add(bill);
        }
        return result;
    }

    public void save() {
        try {
            DataStore.saveBills(bills);
        } catch (IOException e) {
            throw new RuntimeException("Could not save bill data.", e);
        }
    }
}
