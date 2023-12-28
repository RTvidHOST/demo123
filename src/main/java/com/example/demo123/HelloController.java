package com.example.demo123;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
public class HelloController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Button Reg;
    @FXML
    private TextField login;
    @FXML
    private Button adminBut;
    @FXML
    private PasswordField password;
    @FXML
    private Button signIn;
    @FXML
    void initialize() {
        Reg.setOnAction(event -> {
            openSecondWindow(event);
        });
        signIn.setOnAction(event -> {
            String log = login.getText().trim();
            String pass = password.getText().trim();

            if (!log.equals("") && !pass.equals(""))
                loginMethod(log, pass);
            else
                showAlert("Заполните поля");
        });
        adminBut.setOnAction(event -> {
            String loginAdmin = login.getText().trim();
            String passAdmin = password.getText().trim();
            regAdmin(event);

            if (!loginAdmin.equals("") && !passAdmin.equals(""))
                loginAdminMethod(loginAdmin, passAdmin);
            else
                showAlert("Заполните поля");
        });
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
    private void loginAdminMethod(String loginAdmin, String passAdmin) {
        Admin admin = new Admin();
        admin.setLogin(loginAdmin);
        admin.setPassword(passAdmin);
        ResultSet result = getAdmin(admin);
        int counter = 0;
        while (true) {
            try {
                if (!result.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            counter++;
        }
        if (counter >= 1) {
            adminBut.setOnAction(event -> {
                openAdminWindow(event);
            });
        } else
            showAlert("Ошибка");
    }
    private void openAdminWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminPane.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openUserWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userPane.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String username1;
    public void loginMethod(String log, String pass) {
        User user = new User();
        user.setLogin(log);
        user.setPassword(pass);
        ResultSet result = getUser(user);
        int counter = 0;
        username1 = log;
        while (true) {
            try {
                if (!result.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            counter++;
        }
        if (counter >= 1) {
            signIn.setOnAction(event -> {
                signIn.getScene().getWindow().hide();
                openUserWindow(event);
            });
        } else
            showAlert("Ошибка");
    }
    public String getLog(){
        return username1;
    }
    public ResultSet getUser(User user){
        ResultSet resultSet = null;
        String select = "SELECT * FROM users WHERE login = ? AND password = ?";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mobilephone",
                    "root", "1747");
            PreparedStatement prSt = connection.prepareStatement(select);
            prSt.setString(1, user.getLogin());
            prSt.setString(2, user.getPassword());
            resultSet = prSt.executeQuery();
            user.setLogin(login.getText().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet getAdmin(Admin admin){
        ResultSet resultSet = null;
        String select = "SELECT * FROM admin WHERE login = ? AND password = ?";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mobilephone",
                    "root", "1747");
            PreparedStatement prSt = connection.prepareStatement(select);
            prSt.setString(1, admin.getLogin());
            prSt.setString(2, admin.getPassword());
            resultSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void openSecondWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("regPane.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}