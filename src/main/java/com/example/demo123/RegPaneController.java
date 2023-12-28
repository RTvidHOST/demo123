package com.example.demo123;

import java.net.URL;
import java.sql.*;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
public class RegPaneController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private Button regBut;
    @FXML
    void initialize() {
        regBut.setOnAction(event -> {
            regMethod(event);
            regAdmin(event);
        });
        regBut.setOnAction(event -> {
            String loginAdmin = login.getText().trim();
            String passAdmin = password.getText().trim();
            regAdmin(event);

            if (!loginAdmin.equals("") && !passAdmin.equals(""))
                regMethod(event);
            else
                showAlert("Заполните поля");
        });
    }
    private static String username1;
    public void regMethod(ActionEvent event) {
        String logintext = login.getText();
        String passwordtext = password.getText();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mobilephone",
                    "root", "1747");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE login = ?");
            statement.setString(1, logintext);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                showAlert("Пользователь уже существует");
            } else {
                statement = connection.prepareStatement("INSERT INTO users (login, password) VALUES (?, ?)");
                statement.setString(1, logintext);
                statement.setString(2, passwordtext);
                statement.executeUpdate();
                showAlert("Регистрация прошла успешно");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void regAdmin(ActionEvent event) {
        String logintext = login.getText();
        String passwordtext = password.getText();
        String adminLogin = "admin";
        String adminPassword = "admin";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mobilephone",
                    "root", "1747");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM admin WHERE login = ?");
            statement.setString(1, adminLogin);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
            } else {
                statement = connection.prepareStatement("INSERT INTO admin (login, password) VALUES (?, ?)");
                statement.setString(1, adminLogin);
                statement.setString(2, adminPassword);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}