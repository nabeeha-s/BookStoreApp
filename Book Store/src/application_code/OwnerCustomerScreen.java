package application_code; //new code

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class OwnerCustomerScreen extends Application {
    private TableView<Person> table = new TableView<>(); //the table to display customer entries
    private static final ObservableList<Person> data = FXCollections.observableArrayList(); //we create an observable list to store customers
    final HBox hb = new HBox(); //the horizontal box for input fields and add button is created and labeled as hb

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ClassLogin.setCustomerList(data); //we update the login system with our customer data
        
        primaryStage.setTitle("Bookstore App"); //set the title of the application window
        primaryStage.setWidth(450); //set the width of the GUI window
        primaryStage.setHeight(550); //set the height of the GUI window

        Label titleLabel = new Label("Bookstore App"); //we create a label for the title of the screen
        titleLabel.setFont(new Font("Arial", 20)); //set the font size and structure

        table.setEditable(true); //we make the table editable, so it can change the names, passwords and points if wrongfully done so

        // Username Column
        TableColumn<Person, String> usernameCol = new TableColumn<>("Username"); //creating a column for customer usernames
        usernameCol.setMinWidth(100); //setting the minimum width
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username")); //we combine the username property
        usernameCol.setCellFactory(TextFieldTableCell.forTableColumn()); //this allows the column in usernames to be text edited
        usernameCol.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            person.setUsername(event.getNewValue()); //we update username when edited
            ClassLogin.setCustomerList(data); // update login system when username changes
        });

        // Password Column
        TableColumn<Person, String> passwordCol = new TableColumn<>("Password"); //creating a column for customer passwords
        passwordCol.setMinWidth(100); //setting the minimum width
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password")); //we combine the password property
        passwordCol.setCellFactory(TextFieldTableCell.forTableColumn()); //this allows the column in passwords to be text edited
        passwordCol.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            person.setPassword(event.getNewValue()); //we update password when edited
            ClassLogin.setCustomerList(data); // update login system when password changes
        });
        
        // Points Column (now with observable property)
        TableColumn<Person, Integer> pointsCol = new TableColumn<>("Points"); //creating a column for customer points
        pointsCol.setMinWidth(100); //setting the minimum width
        pointsCol.setCellValueFactory(cellData -> cellData.getValue().pointsProperty().asObject()); //we use observable property for points
        pointsCol.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            person.setPoints(event.getNewValue()); //we update points when edited
            ClassLogin.setCustomerList(data); // update login system when points change
        });

        table.setItems(data); //setting data for the table
        table.getColumns().addAll(usernameCol, passwordCol, pointsCol); //add the columns to the table

        // Add Customer Controls
        TextField addUsername = new TextField(); //creating the text field for adding new usernames, this is where you can write inside the GUI
        addUsername.setPromptText("Username"); //placeholder text for username
        addUsername.setMaxWidth(usernameCol.getPrefWidth()); //column width is established

        TextField addPassword = new TextField(); //creating the text field for adding new passwords, this is where you can write inside the GUI
        addPassword.setPromptText("Password"); //placeholder text for password
        addPassword.setMaxWidth(passwordCol.getPrefWidth()); //column width is established

        Button addButton = new Button("Add"); //creating a button to add customers to the list
        addButton.setOnAction(e -> {
            String username = addUsername.getText().trim(); //get and trim the username input
            String password = addPassword.getText().trim(); //get and trim the password input
            if (!username.isEmpty() && !password.isEmpty()) { //check if username and password are not empty
                data.add(new Person(username, password, 0)); //adding customer to list with 0 initial points
                ClassLogin.setCustomerList(data); //update the login system with new data
                addUsername.clear(); //clearing input fields after adding
                addPassword.clear(); //clearing password field after adding
            }
        });

        hb.getChildren().addAll(addUsername, addPassword, addButton); //the 'hb' variable creates a horizontal layout for input fields and add button
        hb.setSpacing(10); //set spacing between elements
        hb.setAlignment(Pos.CENTER); //center the input fields and button

        // Action Buttons
        HBox buttonBox = new HBox(10); //create a horizontal layout for the back and delete buttons
        Button backButton = new Button("Back"); //creating the back button
        backButton.setOnAction(e -> showOwnerStartScreen(primaryStage)); //navigates back to the start screen when clicked

        Button deleteButton = new Button("Delete"); //creating the delete button
        deleteButton.setOnAction(e -> {
            Person selectedPerson = table.getSelectionModel().getSelectedItem(); //get the selected customer
            if (selectedPerson != null) {
                data.remove(selectedPerson); //remove customer from list
                ClassLogin.setCustomerList(data); //update login system after deletion
            }
        });

        buttonBox.getChildren().addAll(backButton, deleteButton); //add the buttons to the button box
        buttonBox.setAlignment(Pos.CENTER); //align them to center

        // Main Layout
        VBox vbox = new VBox(10); //this creates a vertical layout for all elements
        vbox.setPadding(new Insets(10)); //add padding around the layout
        vbox.setAlignment(Pos.CENTER); //center align the elements
        vbox.getChildren().addAll(titleLabel, table, hb, buttonBox); //add all elements to the vertical layout

        Scene scene = new Scene(vbox, 450, 550); //creates and set the scene for the stage, the structure of the GUI window pop up initial
        scene.getRoot().setStyle("-fx-background-color: #FFF9BE;");
        primaryStage.setScene(scene); //set the scene to the primary stage
        primaryStage.show(); //show application window
    }

    private void showOwnerStartScreen(Stage primaryStage) {
        OwnerStartScreen.showOwnerStartScreen(primaryStage); //method to navigate back to the owner start screen
    }

    public static ObservableList<Person> getCustomerData() {
        return data; //method to get customer data from elsewhere in the application
    }

    public static void updateCustomerPoints(String username, int newPoints) {
        for (Person person : data) {
            if (person.getUsername().equals(username)) { //find the customer by username
                person.setPoints(newPoints); //update their points
                break;
            }
        }
        ClassLogin.setCustomerList(data); //update login system with modified data
    }

    public static class Person {
        private String username;
        private String password;
        private final SimpleIntegerProperty points; //observable property for points to enable table updates

        public Person(String username, String password, int points) { //constructor to initialize customer details
            this.username = username;
            this.password = password;
            this.points = new SimpleIntegerProperty(points);
        }

        public String getUsername() {
            return username; //get username method
        }

        public void setUsername(String username) {
            this.username = username; //set username method
        }

        public String getPassword() {
            return password; //get password method
        }

        public void setPassword(String password) {
            this.password = password; //set password method
        }
        
        public int getPoints() {
            return points.get(); //get points method
        }
        
        public void setPoints(int points) {
            this.points.set(points); //set points method
        }
        
        public IntegerProperty pointsProperty() {
            return points; //property accessor method for JavaFX binding
        }
    }

    public static void showOwnerCustomerScreen(Stage primaryStage) {
        try {
            OwnerCustomerScreen screen = new OwnerCustomerScreen(); //create a new instance of the screen
            screen.start(primaryStage); //start the screen with the given stage
        } catch (Exception e) {
            e.printStackTrace(); //print any errors that might occur
        }
    }
}