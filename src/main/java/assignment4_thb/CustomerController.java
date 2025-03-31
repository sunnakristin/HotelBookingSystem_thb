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


public class CustomerController {

    @FXML
    public Button fxBackToLoginButton;

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
            Scene scene = new Scene(root);
            registerStage.setScene(scene);
            registerStage.setTitle("Register New Account");

            // Make the new window modal (optional) and set its owner to the current window
            Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            registerStage.initOwner(primaryStage);
            registerStage.initModality(Modality.APPLICATION_MODAL);

            // Show the window and wait until it's closed
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
