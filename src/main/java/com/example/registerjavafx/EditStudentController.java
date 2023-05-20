package com.example.registerjavafx;

import entity.ClassesEntity;
import entity.StudentsEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditStudentController implements Initializable {
    public StudentsEntity studentsEntity;
    @FXML
    ComboBox<StudentsEntity.StudentCondition> stateCombobox;
    @FXML
    TextField nameField, surnameField, yearField, emailField, passwordField;
    @FXML
    Label errorMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stateCombobox.getItems().addAll(StudentsEntity.StudentCondition.values());
    }
    @FXML
    public void acceptButtonHandle(ActionEvent e){
        try {
            String sName = nameField.getText();
            String sSurname = surnameField.getText();
            Integer sYear = Integer.valueOf(yearField.getText());
            String sEmail = emailField.getText();
            String sPassword = passwordField.getText();

            if (sName.equals("") || sSurname.equals("") || sEmail.equals("") || sPassword.equals("") || stateCombobox.getSelectionModel().isEmpty()) {
                errorMessage.setText("Uzupełnij rubryki");
            } else {
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");
                SessionFactory factory = configuration.buildSessionFactory();
                Session session = factory.openSession();
                Transaction tx = session.beginTransaction();
                StudentsEntity studentsE = session.get(StudentsEntity.class, studentsEntity.getStudentId());

                if(studentsE==null){
                    errorMessage.setText("Nie istnieje taki student w bazie danych.");
                }else{
                    System.out.println("Przypisanie.");
                    studentsE.setName(sName);
                    studentsE.setSurname(sSurname);
                    studentsE.setBirthYear(sYear);
                    studentsE.setEmail(sEmail);
                    studentsE.setPassword(sPassword);
                }

                session.update(studentsE);
                tx.commit();
                session.close();


                ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
            }
        }catch(NumberFormatException Ex){
            errorMessage.setText("Podaj właściwy rok.");
        }catch(Exception Ex){
            System.out.println(Ex);
        }
    }
    @FXML
    public void cancelButtonHandle(ActionEvent e){
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
    }

    public void saveInFile(Object obj, String filename){
        try {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(obj);
            out.close();
            file.close();
            System.out.println("Pomyslnie zapisano obiekt w " + filename);
        }catch(Exception e){
            System.out.println(e);
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
            System.out.println(e);
        }
        return null;
    }
}
