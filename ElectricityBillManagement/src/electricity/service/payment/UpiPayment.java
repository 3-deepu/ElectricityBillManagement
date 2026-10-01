package electricity.service.payment;

import electricity.exception.PaymentException;
import electricity.model.Bill;
import electricity.util.IdGenerator;

public class UpiPayment extends Payment {
    private final String upiId;

    public UpiPayment(Bill bill, String upiId) {
        super(bill);
        this.upiId = upiId;
    }

    @Override
    public String pay() throws PaymentException {
        validateAmount();
        if (!upiId.matches("[A-Za-z0-9._-]{2,}@[A-Za-z]{2,}")) {
            throw new PaymentException("Invalid UPI ID.");
        }
        return IdGenerator.transactionId();
    }

    @Override
    public String getMode() {
        return "UPI";
    }
}
