package electricity.service.payment;

import electricity.exception.PaymentException;
import electricity.model.Bill;

public abstract class Payment {
    protected final Bill bill;

    protected Payment(Bill bill) {
        this.bill = bill;
    }

    public abstract String pay() throws PaymentException;

    public abstract String getMode();

    protected void validateAmount() throws PaymentException {
        if (bill.getTotal() <= 0) {
            throw new PaymentException("Payment amount must be greater than zero.");
        }
        if ("PAID".equals(bill.getStatus())) {
            throw new PaymentException("This bill has already been paid.");
        }
    }
}
