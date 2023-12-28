package com.example.demo123;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
public class adminUpdateController {
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
    private service service;
    @FXML
    void initialize() {
        addButton.setOnAction(event -> {
            saveData();
        });
    }
    public void setService(service service){
        this.service = service;

        model.setText(service.getModel());
        info.setText(service.getInfo());
    }
    public void saveData(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mobilephone",
                    "root", "1747");
            PreparedStatement statement = connection.prepareStatement("UPDATE mobilephoneinfo SET info = " +
                    "'" + info.getText() + "'" + " WHERE model = ?");
            statement.setString(1, service.getModel());
            statement.executeUpdate();
            PreparedStatement statement1 = connection.prepareStatement("UPDATE mobilephoneinfo SET model = " +
                    "'" + model.getText() + "'" + " WHERE model = ?");
            statement1.setString(1, service.getModel());
            statement1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) model.getScene().getWindow();
        stage.close();
    }
}