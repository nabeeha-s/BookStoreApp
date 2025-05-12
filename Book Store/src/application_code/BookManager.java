package application_code;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookManager {
    private static final String BOOKS_FILE = "books.txt";
    
    // Save books to file
    public static void saveBooks(ObservableList<OwnerBookScreen.BookAdds> books) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKS_FILE))) {
            for (OwnerBookScreen.BookAdds book : books) {
                writer.println(book.getName() + "," + book.getPrice());
            }
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }
    
    // Load books from file
    public static ObservableList<Book> loadBooks() {
        ObservableList<Book> books = FXCollections.observableArrayList();
        File file = new File(BOOKS_FILE);
        
        if (!file.exists()) {
            return books; // Return empty list if file doesn't exist
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    books.add(new Book(name, price));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading books: " + e.getMessage());
        }
        
        return books;
    }
}