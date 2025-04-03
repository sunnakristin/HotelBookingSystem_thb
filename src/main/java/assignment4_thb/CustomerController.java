package assignment4_thb;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.Parent;




public class CustomerController {

    @FXML
    public Button fxBackToLoginButton;

    @FXML
    public PasswordField fxVerifyPasswordField;

    @FXML
    public Button fxLoginButton;

    @FXML
    public TextField fxEmailField2;

    @FXML
    public PasswordField fxPasswordField2;

    @FXML
    private Button fxRegisterButton;

    @FXML
    private PasswordField fxPasswordField;

    @FXML
    private TextField fxEmailField;

    @FXML
    private TextField fxNameField;

    @FXML
    private Button fxLogin;

    @FXML
    private Button fxRegister;

    private List<Customer> customers;

    public CustomerController() {
        this.customers = new ArrayList<>();
    }

    @FXML
    public void onRegister(ActionEvent actionEvent) {
        try {
            Stage registerStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            registerStage.setScene(scene);
            registerStage.setTitle("Register New Account");
            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            registerStage.initOwner(primaryStage);
            registerStage.initModality(Modality.APPLICATION_MODAL);
            registerStage.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onLogin(ActionEvent actionEvent) {
        try {
            Stage registerStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            registerStage.setScene(scene);
            registerStage.setTitle("Login to your account");
            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            registerStage.initOwner(primaryStage);
            registerStage.initModality(Modality.APPLICATION_MODAL);
            registerStage.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRegisterForm(ActionEvent event) {
        String name = fxNameField.getText();
        String email = fxEmailField.getText();
        String password = fxPasswordField.getText();
        SignUp(name, email, password);
    }

    @FXML
    public void handleLoginForm(ActionEvent event) {
        String email = fxEmailField2.getText();
        String password = fxPasswordField2.getText();
        Customer customer = signIn(email, password);
        if (customer != null) {
            System.out.println("Login Successful!");
        } else {
            System.out.println("Invalid credentials.");
        }
    }


    @FXML
    public void handleBackToLogin(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    public void handleMyBookings(ActionEvent event) {
        // Your code to handle "My Bookings"
    }

    @FXML
    public void handleSearchHotels(ActionEvent event) {
        // Your code to handle "Search Hotels"
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        // Your code to handle Logout
    }


    public void SignUp(String name, String email, String password) {
        Customer customer = new Customer(name, email, password);
        customers.add(customer);
    }

    public Customer signIn(String email, String password) {
        return customers.stream()
                .filter(c -> c.getEmail().equals(email) && c.verifyPassword(password))
                .findFirst()
                .orElse(null);
    }

    public void logOut() {}
}
