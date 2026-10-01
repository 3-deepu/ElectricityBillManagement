package electricity.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PaymentReceipt implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String transactionId;
    private final String billId;
    private final double amount;
    private final String mode;
    private final LocalDateTime time;

    public PaymentReceipt(String transactionId, String billId, double amount, String mode) {
        this.transactionId = transactionId;
        this.billId = billId;
        this.amount = amount;
        this.mode = mode;
        this.time = LocalDateTime.now();
    }

    public String getTransactionId() { return transactionId; }
    public String getBillId() { return billId; }
    public double getAmount() { return amount; }
    public String getMode() { return mode; }
    public LocalDateTime getTime() { return time; }
}
