package application_code;
//adding the neccesary libraries
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
//the public class is created named Login, which is the GUI presentation
public class Login extends Application {
    //this declares the UI components needed on the GUI page
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Label messageLabel;
    private ClassLogin login; //the ClassLogin() class, is referenced as 'login' to use in the JavaFX GUI page
    private static OwnerCustomerScreen.Person loggedInCustomer; //stores the customer who is currently logged in
    
    @Override
    public void start(Stage primaryStage) { //creating a 'start' class
        //then initializing the Login class
        login = new ClassLogin();
        // load customer data from OwnerCustomerScreen if available
        if (ClassLogin.getCustomerList() == null) {
            // initialize with empty list or load from persistent storage if implemented
            // for now, we'll initialize it in OwnerCustomerScreen
        }
        
        //creating the UI elements for the start class specifically
        usernameField = new TextField();
        passwordField = new PasswordField();
        loginButton = new Button("Login");
        messageLabel = new Label();
        
        //the GUI requires placeholder text for fields, and it is labeled as the username, and password
        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");
        
        //setting up the login button action that extends to the goToLogin class
        loginButton.setOnAction(e -> goToLogin(primaryStage));
        loginButton.setStyle("-fx-background-color: #fa940c;");
        
        //creating the vertical box to hold the UI components, setting the paramters and positiing the component names in the center
        VBox vbox = new VBox(10, usernameField, passwordField, loginButton, messageLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-padding: 20;");
        
        //the GUI scene is created and is set it on the stage. the paramters of the dimensions are made, and the title of the GUI is displayed.
        Scene scene = new Scene(vbox, 300, 200);
        scene.getRoot().setStyle("-fx-background-color: lightblue;");
        primaryStage.setTitle("Login System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    //the goToLogin class checks to see who is logging in, the customer or owner
    private void goToLogin(Stage primaryStage) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        //creating boolean methods to check authentication and customer status
        boolean isAuthenticated = login.authenticate(username, password);
        boolean isCustomer = login.isCustomer(username, password);
        
        if (isAuthenticated) {
            // if isAuthenticated, that means that the owner is logging in and then the OwnerStartScreen should open up
            OwnerStartScreen.showOwnerStartScreen(primaryStage);
        } else if (isCustomer) {
            // if the user is a customer, find and store their information
            ObservableList<OwnerCustomerScreen.Person> customers = ClassLogin.getCustomerList();
            if (customers != null) {
                for (OwnerCustomerScreen.Person person : customers) {
                    if (username.equals(person.getUsername()) && password.equals(person.getPassword())) {
                        loggedInCustomer = person;
                        break;
                    }
                }
            }
            
            // redirect to CustomerStartScreen for customer login
            CustomerStartScreen.showCustomerStartScreen(primaryStage);
        } else {
            // if authentication fails, display error message
            messageLabel.setText("Invalid username or password");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }
    
    // method to get the logged-in customer
    public static OwnerCustomerScreen.Person getLoggedInCustomer() {
        return loggedInCustomer;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}