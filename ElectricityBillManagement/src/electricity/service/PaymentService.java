package electricity.service;

import electricity.exception.PaymentException;
import electricity.model.Bill;
import electricity.model.PaymentReceipt;
import electricity.service.payment.*;
import electricity.util.Animation;

public class PaymentService {

    public PaymentReceipt pay(Bill bill, int choice, String credential) throws PaymentException {
        Payment payment;

        switch (choice) {
            case 1 -> payment = new UpiPayment(bill, credential);
            case 2 -> payment = new CardPayment(bill, credential);
            case 3 -> payment = new CashPayment(bill);
            default -> throw new PaymentException("Invalid payment mode.");
        }

        Animation.loading("Processing " + payment.getMode() + " payment", 1);

        String transactionId = payment.pay();
        bill.markPaid(payment.getMode(), transactionId);

        return new PaymentReceipt(
                transactionId,
                bill.getBillId(),
                bill.getTotal(),
                payment.getMode()
        );
    }
}
