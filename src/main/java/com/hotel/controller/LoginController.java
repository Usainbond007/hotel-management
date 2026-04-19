package com.hotel.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password.");
            return;
        }

        if (username.equals(ADMIN_USER) && password.equals(ADMIN_PASS)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
                Scene scene = new Scene(loader.load(), 1100, 700);
                scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setTitle("Alpine Chalet — Management System");
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                errorLabel.setText("Error loading application.");
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Invalid username or password.");
            passwordField.clear();
        }
    }
}