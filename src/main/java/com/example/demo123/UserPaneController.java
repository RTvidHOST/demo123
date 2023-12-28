package com.example.demo123;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class UserPaneController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<service, String> info;

    @FXML
    private TableColumn<service, String> model;

    @FXML
    private Button refresh;

    @FXML
    private TextField search;

    @FXML
    private Button searchBut;

    @FXML
    private TableView<service> service;
    ObservableList<service> serviceData = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        try {
            initService();
            model.setCellValueFactory(new PropertyValueFactory<>("model"));
            info.setCellValueFactory(new PropertyValueFactory<>("info"));
            service.setItems(serviceData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        searchBut.setOnAction(event -> {
            serviceData.clear();
            try {
                initSearch();
                model.setCellValueFactory(new PropertyValueFactory<>("model"));
                info.setCellValueFactory(new PropertyValueFactory<>("info"));
                service.setItems(serviceData);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        refresh.setOnAction(event -> {
            service.setItems(serviceData);
            refreshTable();
        });
    }

    private void refreshTable() {
        serviceData.clear();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mobilephone",
                    "root", "1747");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM mobilephoneinfo");
            while (resultSet.next()){
                String name = resultSet.getString("model");
                String info = resultSet.getString("info");

                serviceData.add(new service(name, info));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initSearch() throws SQLException{
        ResultSet resultSet = dataService1();
        while (resultSet.next()){
            serviceData.add(new service(resultSet.getString("model"),
                    resultSet.getString("info")));
        }
    }

    private ResultSet dataService1() {
        String text = search.getText().toString();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mobilephone",
                    "root", "1747");
            ResultSet resultSet = null;
            String select = "SELECT * FROM mobilephoneinfo WHERE model = " + "'" + text + "'";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(select);
                resultSet = preparedStatement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initService() throws SQLException{
        ResultSet resultSet = dataService();
        while (resultSet.next()){
            serviceData.add(new service(resultSet.getString("model"),
                    resultSet.getString("info")));
        }
    }

    private ResultSet dataService() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mobilephone",
                    "root", "1747");
            ResultSet resultSet = null;
            String select = "SELECT * FROM mobilephoneinfo";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(select);
                resultSet = preparedStatement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}