package application_code;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;

public class Book {
    private final StringProperty name;
    private final StringProperty price;
    private final CheckBox select;
    
    public Book(String name, double price) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleStringProperty(String.format("%.2f", price)); // Convert double to String
        this.select = new CheckBox();
    }
    
    public final StringProperty nameProperty() {
        return name;
    }
    
    public final StringProperty priceProperty() {
        return price;
    }
    
    public String getName() {
        return name.get();
    }
    
    public void setName(String newName) {
        name.set(newName);
    }
    
    public String getPrice() {
        return price.get();
    }
    
    public void setPrice(String newPrice) {
        price.set(newPrice);
    }
    
    public CheckBox getSelect() {
        return select;
    }
    
    public void setSelect(CheckBox select) {
        this.select.setSelected(select.isSelected());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }
        Book b = (Book) o;
        return name.get().equals(b.getName()) && price.get().equals(b.getPrice());
    }
}