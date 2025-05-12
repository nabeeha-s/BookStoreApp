package application_code;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OwnerStartScreen { //the ownerstartscreen class is maded that will come after, if the owner is logged in

    public static void showOwnerStartScreen(Stage primaryStage) { 
        //we list the title of the GUI which should be "Bookstore App"
        Label titleLabel = new Label("Bookstore App");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;"); //dimensions of the title here

        //This declares the UI components needed on the GUI page for the buttons
        Button booksButton = new Button("Books");
        Button customerButton = new Button("Customer");
        Button logoutButton = new Button("Logout");

        //The button actions are declared and where they travel once it is pressed 
        booksButton.setOnAction(e -> goToBookScreen(primaryStage));
        customerButton.setOnAction(e -> goToCustomerScreen(primaryStage));
        logoutButton.setOnAction(e -> logout(primaryStage));

        //creating structrual dimenstions for the buttons
        booksButton.setMinWidth(150);
        customerButton.setMinWidth(150);
        logoutButton.setMinWidth(150);

        //the buttons are placed in a vertical box, title in the center.
        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(titleLabel, booksButton, customerButton, logoutButton);

        //creating the GUI screen dimenstions
        Scene scene = new Scene(vbox, 400, 300);
        scene.getRoot().setStyle("-fx-background-color: #DEE2FC;");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bookstore App");
        primaryStage.show();
    }

    private static void goToBookScreen(Stage primaryStage) { //the goToBookScreen class checks to see if the books button is pressed, if so, then it goes there
        OwnerBookScreen bookScreen = new OwnerBookScreen();
        try {
            bookScreen.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void goToCustomerScreen(Stage primaryStage) { //the goToCustomerScreen class checks to see if the customers button is pressed, if so, then it goes there
        OwnerCustomerScreen customerScreen = new OwnerCustomerScreen();
        try {
            customerScreen.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void logout(Stage primaryStage) { //the logout class checks to see if the logout button is pressed, if so, then it goes back to the login screen
        Login login = new Login();
        try {
            login.start(new Stage());
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}