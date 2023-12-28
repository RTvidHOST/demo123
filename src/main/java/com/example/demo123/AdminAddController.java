package com.example.demo123;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
public class AdminAddController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Button addButton;
    @FXML
    private TextField model;
    @FXML
    private TextField info;
    @FXML
    void initialize() {
        addButton.setOnAction(event -> {
            AddMethod(event);
        });
    }
    public void AddMethod(ActionEvent event) {
        String nametext = model.getText();
        String pricetext = info.getText();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mobilephone",
                    "root", "1747");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM mobilephoneinfo WHERE model = ?");
            statement.setString(1, nametext);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                showAlert("Уже существует");
            } else {
                statement = connection.prepareStatement("INSERT INTO mobilephoneinfo (model, info) VALUES (?, ?)");
                statement.setString(1, nametext);
                statement.setString(2, pricetext);
                statement.executeUpdate();
                showAlert("Успешно добавлено");
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