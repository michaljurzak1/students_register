package com.example.registerjavafx;

import entity.ClassesEntity;
import entity.StudentsEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import java.util.Arrays;
import java.util.List;

public class AddClassController {
    @FXML
    TextField nameField, maxField;
    @FXML
    Label errorMessage;
    @FXML
    public void acceptButtonHandle(ActionEvent e){
        try {
            String groupName = nameField.getText();
            Integer groupMax = Integer.valueOf(maxField.getText());
            if (groupName.equals("")) {
                errorMessage.setText("Uzupełnij rubryki");
            } else {
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");
                Session session = configuration.buildSessionFactory().openSession();
                ClassesEntity classesEntity = new ClassesEntity();
                classesEntity.setGroupName(groupName);
                classesEntity.setMaxNumber(groupMax);
                session.save(classesEntity);
                session.close();

                ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
            }
        }catch(NumberFormatException Ex){
            errorMessage.setText("Podaj właściwą liczbą.");
        }catch(Exception Ex){
            System.out.println(Ex);
        }
    }
    @FXML
    public void cancelButtonHandle(ActionEvent e){
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
    }
}
