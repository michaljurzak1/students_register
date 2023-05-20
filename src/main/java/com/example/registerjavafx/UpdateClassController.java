package com.example.registerjavafx;

import entity.ClassesEntity;
import entity.StudentsEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Arrays;

public class UpdateClassController {
    @FXML
    TextField nameField, maxField;
    String name;
    Integer classID;
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
                    SessionFactory factory = configuration.buildSessionFactory();
                    Session session = factory.openSession();
                    Transaction tx = session.beginTransaction();
                    ClassesEntity klasa = session.get(ClassesEntity.class, classID);
                    if(klasa==null){
                        errorMessage.setText("Klasa ta nie jest w bazie danych.");
                    }
                    else if (klasa.getStudentsNumber() <= groupMax) {
                        klasa.setMaxNumber(groupMax);
                        klasa.setGroupName(nameField.getText());

                        session.update(klasa);
                        tx.commit();
                        session.close();

                        ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
                    } else {
                        errorMessage.setText("Nie można zmienić na taki rozmiar.");
                    }
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
