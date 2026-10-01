package electricity.model;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String customerId;
    private String name;
    private String mobile;
    private String address;
    private final String username;

    private double previousReading;
    private double currentReading;

    public Customer(String customerId, String name, String mobile, String address, String username) {
        this.customerId = customerId;
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.username = username;
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getMobile() { return mobile; }
    public String getAddress() { return address; }
    public String getUsername() { return username; }
    public double getPreviousReading() { return previousReading; }
    public double getCurrentReading() { return currentReading; }

    public void setName(String name) { this.name = name; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setAddress(String address) { this.address = address; }
    public void setCurrentReading(double currentReading) { this.currentReading = currentReading; }
    public void setPreviousReading(double previousReading) { this.previousReading = previousReading; }
}
