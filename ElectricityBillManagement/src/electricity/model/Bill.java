package electricity.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Bill implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String billId;
    private final String customerId;
    private final LocalDate billDate;
    private final LocalDate dueDate;
    private final double units;
    private final double energyCharge;
    private final double fixedCharge;
    private final double lateFee;
    private final double total;

    private String status;
    private String paymentMode;
    private String transactionId;

    public Bill(String billId, String customerId, LocalDate billDate, LocalDate dueDate,
                double units, double energyCharge, double fixedCharge,
                double lateFee, double total) {
        this.billId = billId;
        this.customerId = customerId;
        this.billDate = billDate;
        this.dueDate = dueDate;
        this.units = units;
        this.energyCharge = energyCharge;
        this.fixedCharge = fixedCharge;
        this.lateFee = lateFee;
        this.total = total;
        this.status = "UNPAID";
        this.paymentMode = "NONE";
        this.transactionId = "NONE";
    }

    public String getBillId() { return billId; }
    public String getCustomerId() { return customerId; }
    public LocalDate getBillDate() { return billDate; }
    public LocalDate getDueDate() { return dueDate; }
    public double getUnits() { return units; }
    public double getEnergyCharge() { return energyCharge; }
    public double getFixedCharge() { return fixedCharge; }
    public double getLateFee() { return lateFee; }
    public double getTotal() { return total; }
    public String getStatus() { return status; }
    public String getPaymentMode() { return paymentMode; }
    public String getTransactionId() { return transactionId; }

    public void markPaid(String paymentMode, String transactionId) {
        this.status = "PAID";
        this.paymentMode = paymentMode;
        this.transactionId = transactionId;
    }
}
