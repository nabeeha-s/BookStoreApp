package application_code;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
public class ClassLogin { //creating a classlogin class
    // these are the owners username and password, strictly confirmed
    private static final String OWNER_USERNAME = "admin";
    private static final String OWNER_PASSWORD = "admin";
    
    // this is to store invalid login attempts
    private List<String> failedLogins;
    
    // static observablelist to store customer credentials that can be accessed from other classes
    private static ObservableList<OwnerCustomerScreen.Person> customerList;
    
    // the constructor for ClassLogin is made to initialize the failed logins list
    public ClassLogin() {
        failedLogins = new ArrayList<>();
    }
    
    // method to set the customer list from OwnerCustomerScreen
    public static void setCustomerList(ObservableList<OwnerCustomerScreen.Person> list) {
        customerList = list;
    }
    
    // static method to get the customer list
    public static ObservableList<OwnerCustomerScreen.Person> getCustomerList() {
        return customerList;
    }
    
    // if the boolean "authenticated" class reads the owner username and password to be "admin" and "admin", then return true
    public boolean authenticate(String username, String password) {
        // check if username and password match the owner credentials
        if (username.equals(OWNER_USERNAME) && password.equals(OWNER_PASSWORD)) {
            return true; // owner authentication successful
        } 
        
        // check if username and password match any customer in the list
        if (customerList != null) {
            for (OwnerCustomerScreen.Person customer : customerList) {
                if (username.equals(customer.getUsername()) && password.equals(customer.getPassword())) {
                    return false; // customer authentication successful, but we return false to differentiate from owner
                }
            }
        }
        
        // this is where we store the failed login attempts
        failedLogins.add("Failed login attempt: Username: " + username + ", Password: " + password);
        return false;
    }
    
    // check specifically if this is a customer login
    public boolean isCustomer(String username, String password) {
        if (customerList != null) {
            for (OwnerCustomerScreen.Person customer : customerList) {
                if (username.equals(customer.getUsername()) && password.equals(customer.getPassword())) {
                    return true; // customer authentication successful
                }
            }
        }
        return false;
    }
    
    // returning the failed login attempts
    public List<String> getFailedLogins() {
        return failedLogins;
    }
}