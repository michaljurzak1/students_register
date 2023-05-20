package com.example.registerjavafx;

import entity.ClassesEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Main extends Application {
    Controller mainController;
    @Override
    public void start(Stage stage) throws IOException {
        Controller controller = new Controller();
        mainController = controller;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Parent loginRoot = fxmlLoader.load();
        LoginController loginController = fxmlLoader.getController();
        loginController.mainController = controller;



        Scene scene = new Scene(loginRoot);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}