package assignment4_thb;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.Parent;




public class CustomerController {

    @FXML public Button fxBackToLoginButton;

    @FXML public PasswordField fxVerifyPasswordField;
    @FXML public Button fxLoginButton;
    @FXML public TextField fxEmailField2;
    @FXML public PasswordField fxPasswordField2;
    @FXML private Button fxRegisterButton;
    @FXML private PasswordField fxPasswordField;
    @FXML private TextField fxEmailField;
    @FXML private TextField fxNameField;
    @FXML private Button fxLogin;
    @FXML private Button fxRegister;

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
            System.out.println("Error loading: " + e.getMessage());
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
        String verifyPassword = fxVerifyPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || verifyPassword.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        if (!password.equals(verifyPassword)) {
            showAlert("Error", "Passwords do not match.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:hotel.db")) {
            String insertSQL = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertSQL);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);

            int result = stmt.executeUpdate();
            if (result > 0) {
                showAlert("Success", "Registration successful!");
                ((Node) event.getSource()).getScene().getWindow().hide();
            } else {
                showAlert("Error", "Registration failed, please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error: " + e.getMessage());
        }
    }

    @FXML
    public void handleLoginForm(ActionEvent event) {
        String email = fxEmailField2.getText().trim();
        String password = fxPasswordField2.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Email and password are required!");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:hotel.db")) {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String userName = rs.getString("name");
                Customer customer = new Customer(userName, email, password);

                // Close the login window
                Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                loginStage.close();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("profileAndSearch-view.fxml"));
                    Parent root = loader.load();

                    // Pass the customer to the next controller
                    SearchController controller = loader.getController();
                    controller.setCurrentCustomer(customer);

                    // Create and show the new window without setting the owner/modality
                    Stage profileStage = new Stage();
                    profileStage.setScene(new Scene(root));
                    profileStage.setTitle("Profile and Search");
                    profileStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Could not load the Profile and Search window: " + e.getMessage());
                }
            } else {
                showAlert("Error", "Invalid credentials or user not registered.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error: " + e.getMessage());
        }
    }


    @FXML
    public void handleBackToLogin(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
