package com.example.registerjavafx;

import entity.StudentsEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable{
    public Controller mainController = new Controller();
    @FXML
    TextField emailField;
    @FXML
    FXMLLoader loader;
    @FXML
    Parent root;
    @FXML
    PasswordField passwordField;
    @FXML
    Button loginButton, adminLoginButton, adminClientButton;
    @FXML
    Label errorLabel;
    boolean isLogged = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setTooltip(new Tooltip("Zaloguj się"));
        passwordField.setTooltip(new Tooltip("Pole obowiązkowe."));
        emailField.setTooltip(new Tooltip("Pole obowiązkowe."));

        try {
            loader = new FXMLLoader(getClass().getResource("main-panel.fxml"));
            root = loader.load();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    @FXML
    public void handleAdminClientButton(ActionEvent event) throws IOException{

        if(loginButton.isVisible() && !adminLoginButton.isVisible()){
            loginButton.setVisible(false);
            adminLoginButton.setVisible(true);
            adminClientButton.setText("Klient");

            loader = new FXMLLoader(getClass().getResource("admin-panel.fxml"));
            root = loader.load();
        }else{
            loginButton.setVisible(true);
            adminLoginButton.setVisible(false);
            adminClientButton.setText("Admin");

            loader = new FXMLLoader(getClass().getResource("main-panel.fxml"));
            root = loader.load();
        }

    }

    @FXML
    public void handleAdminLoginButton(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();
        AdminController controller = loader.getController();

        if(email.equals("admin") && password.equals("admin")){
            isLogged = true;
        }

        if(isLogged){
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Panel Admina");
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(e -> controller.onWindowClose());
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        }else{
            errorLabel.setText("Niepoprawny email lub hasło.");
        }

    }

    @FXML
    public void handleLoginButton(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        Controller controller = loader.getController();
        StudentsEntity studentsEntity = new StudentsEntity();
        StudentsEntity student = studentsEntity.findByEmail(email);

        if(student!=null && student.getPassword().equals(password)){
            isLogged = true;
            System.out.println("Student został poprawnie zalogowany: " + student.getName() + " " + student.getSurname());
        }

        if(isLogged){
            Stage stage = new Stage();
            controller.studentsEntity = student;
            controller.populate();
            controller.refresh();
            Scene scene = new Scene(root);
            stage.setTitle("Główny panel");
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(e -> controller.onWindowClose());
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        }else{
            errorLabel.setText("Niepoprawny email lub hasło.");
        }

    }
    public Object openFromFile(String filename){
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            Object obj = in.readObject();
            in.close();
            file.close();
            System.out.println("Pomyslnie użyto obiektu w " + filename);
            return obj;
        }catch(Exception e){
            System.out.println("Błąd podczas otwierania pliku " + filename);
            System.out.println(e);
        }
        return null;
    }

}
