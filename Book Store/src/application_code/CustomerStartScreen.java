package application_code;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CustomerStartScreen extends Application {
    
    private TableView<Book> table = new TableView<>();
    private ObservableList<Book> data = FXCollections.observableArrayList();
    private Customer customer;
    private String username;
    private int points;
    private Label pointsLabel;
    private Label statusLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bookstore App");
        primaryStage.setWidth(500);
        primaryStage.setHeight(600);

        Label titleLabel = new Label("Bookstore App");
        titleLabel.setFont(new Font("Arial", 20));

        // Welcome message
        Label welcomeLabel = new Label("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", 16));

        // Displays customer points and status
        pointsLabel = new Label("You have " + points + " points.");
        statusLabel = new Label("Your status is " + getStatus(points));

        // Initialize customer object
        customer = new Customer(username, "password", points);
        customer.setPoints(points);

        table.setEditable(true);

        // Book table columns
        TableColumn<Book, String> bookNameCol = new TableColumn<>("Book Name");
        bookNameCol.setMinWidth(200);
        bookNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Book, String> costCol = new TableColumn<>("Cost of Book");
        costCol.setMinWidth(100);
        costCol.setCellValueFactory(cellData -> cellData.getValue().priceProperty());

        TableColumn<Book, CheckBox> selectCol = new TableColumn<>("Select");
        selectCol.setMinWidth(100);
        selectCol.setCellValueFactory(new PropertyValueFactory<>("select"));

        // Load books into table
        data = BookManager.loadBooks();
        table.setItems(data);
        table.getColumns().addAll(bookNameCol, costCol, selectCol);

        // Buttons for purchase, redeem, and logout
        HBox buttonBox = new HBox(10);
        Button buyButton = new Button("Buy");
        Button redeemButton = new Button("Redeem");
        Button logoutButton = new Button("Logout");

        // Buy button action
        buyButton.setStyle("-fx-background-color: #fa940c;");
        buyButton.setOnAction(e -> {
            double totalCost = calculateTotalCost();
            if (totalCost > 0) {
                int earnedPoints = (int) (totalCost * 10); // Earn 10 points per $1 spent
                
                // Update points
                customer.setPoints(customer.getPoints() + earnedPoints);
                updatePointsDisplay();
                
                // Update in owner's view
                OwnerCustomerScreen.updateCustomerPoints(username, customer.getPoints());
                
                // Show cost screen
                CustomerCostScreen.showCustomerCostScreen(
                    primaryStage, 
                    totalCost, 
                    customer.getPoints() - earnedPoints, 
                    customer.getPoints(), 
                    customer.getState().getState()
                );
            }
        });

        // Redeem button action
        redeemButton.setStyle("-fx-background-color: #fa940c;");
        redeemButton.setOnAction(e -> {
            double totalCost = calculateTotalCost();
            CustomerBuyRedeemScreen.showCustomerBuyRedeemScreen(
                primaryStage, 
                totalCost, 
                customer.getPoints(), 
                customer.getState().getState()
            );
        });

        // Logout button action
        logoutButton.setStyle("-fx-background-color: #fa940c;");
        logoutButton.setOnAction(e -> new Login().start(primaryStage));

        buttonBox.getChildren().addAll(buyButton, redeemButton, logoutButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(titleLabel, welcomeLabel, pointsLabel, statusLabel, table, buttonBox);

        Scene scene = new Scene(vbox, 500, 600);
        scene.getRoot().setStyle("-fx-background-color: #FFF9BE;");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Updates the points display on the UI.
     */
    private void updatePointsDisplay() {
        points = customer.getPoints();
        pointsLabel.setText("You have " + points + " points.");
        statusLabel.setText("Your status is " + getStatus(points));
    }

    /**
     * Determines customer status based on points.
     * @return "Gold" if points are 1000 or more, otherwise "Silver"
     */
    private String getStatus(int points) {
        return points >= 1000 ? "Gold" : "Silver";
    }

    /**
     * Calculates the total cost of selected books.
     * @return total cost of selected books
     */
    private double calculateTotalCost() {
        return data.stream()
                .filter(book -> book.getSelect().isSelected())
                .mapToDouble(book -> Double.parseDouble(book.getPrice()))
                .sum();
    }
    
    /**
     * Displays the customer start screen
     */
    public static void showCustomerStartScreen(Stage primaryStage) {
        CustomerStartScreen customerStartScreen = new CustomerStartScreen();
    
        // Get logged-in customer details
        String loggedInUsername = null;
        int loggedInPoints = 0;
    
        ObservableList<OwnerCustomerScreen.Person> customers = ClassLogin.getCustomerList();
        if (customers != null && Login.getLoggedInCustomer() != null) {
            for (OwnerCustomerScreen.Person person : customers) {
                if (person.getUsername().equals(Login.getLoggedInCustomer().getUsername())) {
                    loggedInUsername = person.getUsername();
                    loggedInPoints = person.getPoints();
                    break;
                }
            }
        }
    
        if (loggedInUsername != null) {
            customerStartScreen.setCustomerInfo(loggedInUsername, loggedInPoints);
            Customer customer = new Customer(loggedInUsername, "", loggedInPoints);
            customerStartScreen.setCustomer(customer);
        }
    
        customerStartScreen.start(primaryStage);
    }

    /**
     * Sets the customer's information.=
     */
    public void setCustomerInfo(String username, int points) {
        this.username = username;
        this.points = points;
        if (pointsLabel != null) {
            pointsLabel.setText("You have " + points + " points.");
        }
        if (statusLabel != null) {
            statusLabel.setText("Your status is " + getStatus(points));
        }
    }
    
    /**
     * Sets the customer object and updates the UI
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null) {
            this.points = customer.getPoints();
            if (pointsLabel != null) {
                pointsLabel.setText("You have " + points + " points.");
            }
            if (statusLabel != null) {
                statusLabel.setText("Your status is " + getStatus(points));
            }
        }
    }
}