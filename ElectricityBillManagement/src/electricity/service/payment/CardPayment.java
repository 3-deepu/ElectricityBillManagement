package electricity.service.payment;

import electricity.exception.PaymentException;
import electricity.model.Bill;
import electricity.util.IdGenerator;

public class CardPayment extends Payment {
    private final String cardNumber;

    public CardPayment(Bill bill, String cardNumber) {
        super(bill);
        this.cardNumber = cardNumber;
    }

    @Override
    public String pay() throws PaymentException {
        validateAmount();
        if (!cardNumber.matches("\\d{16}")) {
            throw new PaymentException("Card number must contain exactly 16 digits.");
        }
        return IdGenerator.transactionId();
    }

    @Override
    public String getMode() {
        return "CARD";
    }
}
