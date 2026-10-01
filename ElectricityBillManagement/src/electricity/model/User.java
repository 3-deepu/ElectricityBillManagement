package electricity.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String username;
    private final String password;
    private final String customerId;

    public User(String username, String password, String customerId) {
        this.username = username;
        this.password = password;
        this.customerId = customerId;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getCustomerId() { return customerId; }
}
