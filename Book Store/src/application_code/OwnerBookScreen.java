package application_code;

import javafx.application.Application;
import static javafx.application.Application.launch;
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

public class OwnerBookScreen extends Application { //the OwnerBookScreen class is created and extends Application

    private TableView<BookAdds> table = new TableView<>(); //the table to display book entries, similarly used from the "demoExample" code in module 8
    private final ObservableList<BookAdds> data = FXCollections.observableArrayList(); //we create an observable list to store books
    final HBox hb = new HBox(); //the horizontal box for input fields and add button is created and labeled as hb

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {  
    //set the title of the application window  
    primaryStage.setTitle("Bookstore App");  

    //set the paramters of the GUI window
    primaryStage.setWidth(450);  
    primaryStage.setHeight(550);  

    //we create a label for the title of the screen  
    Label titleLabel = new Label("Bookstore App");  
    titleLabel.setFont(new Font("Arial", 20)); //set the font size and structure

    //we make the table editable, so it can change the names and price if wrongfully done so 
    table.setEditable(true);  

    //creating a column for book names  
    TableColumn<BookAdds, String> nameCol = new TableColumn<>("Name");  
    nameCol.setMinWidth(100); //seting the minimum width  
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name")); //we combine the name property  
    nameCol.setCellFactory(TextFieldTableCell.forTableColumn()); //lthis allows the column in book names to be text edited 
    nameCol.setOnEditCommit(event -> {  
        event.getRowValue().setName(event.getNewValue()); //we update name when edited
        // save books after editing
        BookManager.saveBooks(data); //we save the books to file after changes are made
    });  

    //creating a column for book prcies  
    TableColumn<BookAdds, String> priceCol = new TableColumn<>("Price");  
    priceCol.setMinWidth(100); //seting the minimum width  
    priceCol.setCellValueFactory(new PropertyValueFactory<>("price")); //we combine the name property  
    priceCol.setCellFactory(TextFieldTableCell.forTableColumn()); //lthis allows the column in book names to be text edited  
    priceCol.setOnEditCommit(event -> {  
        event.getRowValue().setPrice(event.getNewValue()); //we update name when edited
        // save books after editing
        BookManager.saveBooks(data); //we save the books to file after changes are made
    });  

    //add the columns to the table  
    table.setItems(data); //setting data for the table  
    table.getColumns().addAll(nameCol, priceCol);  

    //creating the text fields for adding new books, this is where you can write inside the GUI 
    TextField addName = new TextField();  
    addName.setPromptText("Name"); //placeholder text for book name
    addName.setMaxWidth(nameCol.getPrefWidth()); //column width is established

    //creating the text fields for adding new book prices, this is where you can write inside the GUI 
    TextField addPrice = new TextField();  
    addPrice.setPromptText("Price"); //placeholder text for book price
    addPrice.setMaxWidth(priceCol.getPrefWidth()); //column width is established 

    //creating a button to add books to the list  
    Button addButton = new Button("Add");  
    addButton.setOnAction(e -> {  
        data.add(new BookAdds(addName.getText(), addPrice.getText())); //adding book to list  
        addName.clear(); //clearing input fields after adding  
        addPrice.clear(); //and for price
        // save books after adding
        BookManager.saveBooks(data); //we save the books to file after adding a new book
    });  

    //the 'hb' variable creates a horizontal layout for input fields and add button  
    hb.getChildren().addAll(addName, addPrice, addButton);  
    hb.setSpacing(10); //the strcture sets spacing between elements  
    hb.setAlignment(Pos.CENTER); //and centers the names in the button 

    //create a horizontal layout for the back and delete buttons  
    HBox buttonBox = new HBox(10); 
    Button backButton = new Button("Back");  
    backButton.setOnAction(e -> showOwnerStartScreen(primaryStage)); //navigates back to the start screen

    Button deleteButton = new Button("Delete");  //now for the delete button
    deleteButton.setOnAction(e -> {  
        BookAdds selectedBook = table.getSelectionModel().getSelectedItem(); //takes hold of the selected book  
        if (selectedBook != null) {  
            data.remove(selectedBook); //and removes book from list
            // save books after deleting
            BookManager.saveBooks(data); //we save the updated books list to file after deleting
        }  
    });  

    //the buttons to the button box and align them to center  
    buttonBox.getChildren().addAll(backButton, deleteButton);  
    buttonBox.setAlignment(Pos.CENTER);  

    //this creates a vertical layout for all elements  
    VBox vbox = new VBox(10); //sets the structures and spacing  
    vbox.setPadding(new Insets(10));  
    vbox.setAlignment(Pos.CENTER); //the center aligns element names
    vbox.getChildren().addAll(titleLabel, table, hb, buttonBox); 

    //creates and set the scene for the stage, the structure of the GUI window pop up intial  
    Scene scene = new Scene(vbox, 450, 550);  
    scene.getRoot().setStyle("-fx-background-color: #FFDBBB;");
    primaryStage.setScene(scene);  
    primaryStage.show(); //show application window
    
    // load existing books when the screen is opened
    loadExistingBooks(); //we call this method to load any previously saved books when the screen opens
}
    
    // method to load existing books from file
    private void loadExistingBooks() {
        // clear current data
        data.clear(); //we empty the current data list
        
        // create a temporary list to hold book objects
        ObservableList<Book> books = BookManager.loadBooks(); //we load the books from the file
        
        // convert book objects to bookadds objects
        for (Book book : books) {
            data.add(new BookAdds(book.getName(), book.getPrice())); //we add each book to our display list
        }
    }
    
    //method to navigate back to the owner start screen
    private void showOwnerStartScreen(Stage primaryStage) {
        OwnerStartScreen.showOwnerStartScreen(primaryStage);
    }

    //class 'BookAdds' representing Book entries and the output of them
    public static class BookAdds {
        private String name;
        private String price;

        public BookAdds(String theName, String thePrice) { // Constructor to initialize book details
            this.name = theName;
            this.price = thePrice;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}