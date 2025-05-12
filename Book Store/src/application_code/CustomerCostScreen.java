package application_code;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerCostScreen {

    public void start(Stage primaryStage, double totalCost, int originalPoints, int newTotalPoints, String status) {
        // Title label
        Label titleLabel = new Label("Customer Cost Summary");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Cost, points, and status display
        Label costLabel = new Label("Total Cost: $" + String.format("%.2f", totalCost));
        Label totalPointsLabel = new Label("Points: " + newTotalPoints);
        Label statusLabel = new Label("Status: " + status);
        
        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout(primaryStage));

        // Back button to return to shopping
        Button backButton = new Button("Continue Shopping");
        backButton.setOnAction(e -> {
            primaryStage.close();
            // Reopen the customer start screen to continue shopping
            CustomerStartScreen.showCustomerStartScreen(new Stage());
        });

        // Layout
        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(titleLabel,costLabel, totalPointsLabel, statusLabel, backButton, logoutButton);

        // Scene setup
        Scene scene = new Scene(vbox, 400, 400);  // Increased height to accommodate new buttons
        scene.getRoot().setStyle("-fx-background-color: #D0B8DA;");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Customer Cost Summary");
        primaryStage.show();
    }

    // Method to navigate to the Login screen
    private void logout(Stage primaryStage) {
        Login login = new Login();
        try {
            login.start(new Stage()); // Show the login screen
            primaryStage.close(); // Close the current screen
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Updated method to show the cost screen
    public static void showCustomerCostScreen(Stage primaryStage, double totalCost, 
                                           int originalPoints, int newTotalPoints, String status) {
        CustomerCostScreen customerCostScreen = new CustomerCostScreen();
        customerCostScreen.start(primaryStage, totalCost, originalPoints, newTotalPoints, status);
    }
}