package application_code;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Customer {

    private String username;
    private String password;
    private IntegerProperty points;
    private State state;

    // Constructor for a new customer with default 0 points
    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
        this.points = new SimpleIntegerProperty(0);
        this.state = new SilverCustomer();

        // Automatically update the state whenever points change
        this.points.addListener((obs, oldVal, newVal) -> updateState());
    }

    // Constructor for a customer with existing points
    public Customer(String username, String password, int points) {
        this.username = username;
        this.password = password;
        this.points = new SimpleIntegerProperty(points);
        updateState(); // Make sure the correct state is set initially

        // Automatically update the state whenever points change
        this.points.addListener((obs, oldVal, newVal) -> updateState());
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for points property (used in JavaFX bindings)
    public final IntegerProperty pointsProperty() {
        return points;
    }

    // Getter for points as an int
    public int getPoints() {
        return points.get();
    }

    // Setter for points (triggers state update automatically)
    public void setPoints(int points) {
        this.points.set(points);
    }

    // Updates the customer's state based on their points
    private void updateState() {
        if (points.get() < 1000) {
            state = new SilverCustomer();
        } else {
            state = new GoldCustomer();
        }

        // Debugging message to confirm state changes
        System.out.println("State updated: " + state.getState());
    }

    // Getter for the current state
    public State getState() {
        return state;
    }
}