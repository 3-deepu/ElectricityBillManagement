package electricity.service.payment;

import electricity.exception.PaymentException;
import electricity.model.Bill;
import electricity.util.IdGenerator;

public class CashPayment extends Payment {
    public CashPayment(Bill bill) {
        super(bill);
    }

    @Override
    public String pay() throws PaymentException {
        validateAmount();
        return IdGenerator.transactionId();
    }

    @Override
    public String getMode() {
        return "CASH";
    }
}
