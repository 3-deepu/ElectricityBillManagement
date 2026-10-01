package electricity.service;

import electricity.exception.InvalidInputException;
import electricity.model.User;
import electricity.util.DataStore;
import electricity.util.IdGenerator;

import java.io.IOException;
import java.util.ArrayList;

public class AuthService {
    private final ArrayList<User> users;

    public AuthService() {
        users = DataStore.loadUsers();
    }

    public User register(String username, String password, String customerId)
            throws InvalidInputException {
        if (username.length() < 3) throw new InvalidInputException("Username must have at least 3 characters.");
        if (password.length() < 4) throw new InvalidInputException("Password must have at least 4 characters.");
        if (find(username) != null) throw new InvalidInputException("Username already exists.");

        User user = new User(username, password, customerId);
        users.add(user);
        save();
        return user;
    }

    public User login(String username, String password) throws InvalidInputException {
        User user = find(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new InvalidInputException("Invalid username or password.");
        }
        return user;
    }

    public User find(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) return user;
        }
        return null;
    }

    public String generateCustomerId() {
        return IdGenerator.customerId(users.size());
    }

    private void save() {
        try {
            DataStore.saveUsers(users);
        } catch (IOException e) {
            throw new RuntimeException("Could not save user data.", e);
        }
    }
}
