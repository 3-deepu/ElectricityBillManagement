package electricity.util;

import java.util.UUID;

public final class IdGenerator {
    private IdGenerator() {}

    public static String customerId(int size) {
        return String.format("CUS%04d", size + 1001);
    }

    public static String billId(int size) {
        return String.format("BILL%05d", size + 10001);
    }

    public static String transactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
