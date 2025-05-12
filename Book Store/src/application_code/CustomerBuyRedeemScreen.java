package application_code;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerBuyRedeemScreen {

    // Start method to launch the screen with provided parameters
    public void start(Stage primaryStage, double initialCost, int currentPoints, String status) {
        // Title label
        Label titleLabel = new Label("Customer Purchase Summary");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Calculate the discount and new point balance
        int pointsToDeduct = deductPoints(initialCost, currentPoints);
        double discount = pointsToDeduct / 100.0; // 100 points = $1 discount
        double finalCost = Math.max(0, initialCost - discount);

        // Update total points: remaining points + new points earned
        int remainingPoints = currentPoints - pointsToDeduct;
        int newTotalPoints = addPoints(remainingPoints, finalCost);
        
        // In CustomerBuyRedeemScreen, after calculating the new points:
        String username = Login.getLoggedInCustomer().getUsername();
        OwnerCustomerScreen.updateCustomerPoints(username, newTotalPoints);
        
        // New code (what you should change it to)
        Label finalCostLabel = new Label("Total Cost: $" + String.format("%.2f", finalCost));
        Label totalPointsLabel = new Label("Points: " + newTotalPoints);
        String newStatus = newTotalPoints >= 1000 ? "Gold" : "Silver";
        Label statusLabel = new Label("Status: " + newStatus);

        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout(primaryStage));
        
        // Back button to return to shopping
        Button backButton = new Button("Continue Shopping");
        backButton.setOnAction(e -> {
            try {
                System.out.println("Back button clicked, attempting to open CustomerStartScreen");
                System.out.println("Current logged in customer: " + (Login.getLoggedInCustomer() != null ? 
                    Login.getLoggedInCustomer().getUsername() : "null"));
                primaryStage.close();
                CustomerStartScreen.showCustomerStartScreen(new Stage());
            } catch (Exception ex) {
                System.out.println("Error opening CustomerStartScreen: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Layout
        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(titleLabel, finalCostLabel, totalPointsLabel, statusLabel, backButton, logoutButton);

        // Scene setup
        Scene scene = new Scene(vbox, 400, 300);
        scene.getRoot().setStyle("-fx-background-color: #D0B8DA;");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Customer Purchase Summary");
        primaryStage.show();
    }

    // Method to calculate the number of points to deduct
    private int deductPoints(double initialCost, int currentPoints) {
        int maxRedeemablePoints = (int) (initialCost * 100); // Max points that can be used (100 points = $1)
        return Math.min(currentPoints, maxRedeemablePoints);  // Deduct the lesser of available points or required points
    }

    // Method to add points based on the amount spent after discount
    private int addPoints(int remainingPoints, double finalCost) {
        int earnedPoints = (int) (finalCost * 10); // Earn 10 points per $1 spent
        return remainingPoints + earnedPoints;
    }

    // Logout method that navigates to the Login screen
    private void logout(Stage primaryStage) {
        Login login = new Login();
        try {
            login.start(new Stage()); // Show the login screen
            primaryStage.close(); // Close the current screen
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Static method to be called by other classes to display the CustomerBuyRedeemScreen
    public static void showCustomerBuyRedeemScreen(Stage primaryStage, double initialCost, int currentPoints, String status) {
        CustomerBuyRedeemScreen customerBuyRedeemScreen = new CustomerBuyRedeemScreen();
        customerBuyRedeemScreen.start(primaryStage, initialCost, currentPoints, status); // Call start method with parameters
    }
}