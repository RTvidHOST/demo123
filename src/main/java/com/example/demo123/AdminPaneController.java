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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminPaneController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addService;

    @FXML
    private Button delService;

    @FXML
    private TableColumn<service, String> info;

    @FXML
    private TableColumn<service, String> model;

    @FXML
    private Button refresh;

    @FXML
    private TableView<service> serviceTable;

    @FXML
    private Button updateService;

    ObservableList<service> serviceData = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        try {
            initService();
            model.setCellValueFactory(new PropertyValueFactory<>("model"));
            info.setCellValueFactory(new PropertyValueFactory<>("info"));
            serviceTable.setItems(serviceData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        delService.setOnAction(event -> {
            deleteService();
        });
        updateService.setOnAction(event -> {
            openEditWindow();
        });
        refresh.setOnAction(event -> {
            serviceTable.setItems(serviceData);
            refreshTable();
        });
        addService.setOnAction(event -> {
            openAddWindow(event);
        });
    }

    private void openAddWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminAdd.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        serviceData.clear();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mobilephone",
                    "root", "1747");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM mobilephoneinfo");
            while (resultSet.next()){
                String model = resultSet.getString("model");
                String info = resultSet.getString("info");

                serviceData.add(new service(model, info));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void openEditWindow() {
        service selectedData = serviceTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("adminUpdate.fxml"));
        try {
            Parent root = loader.load();
            adminUpdateController updateController = loader.getController();
            updateController.setService(selectedData);
            serviceTable.getItems();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteService() {
        service selectedData = serviceTable.getSelectionModel().getSelectedItem();
        if (selectedData != null) {
            String name = selectedData.getModel();
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mobilephone",
                        "root", "1747");
                PreparedStatement statement = connection.prepareStatement("DELETE FROM mobilephoneinfo WHERE model = ?");
                statement.setString(1, name);
                statement.executeUpdate();
                serviceTable.getItems().remove(selectedData);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void initService() throws SQLException {
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