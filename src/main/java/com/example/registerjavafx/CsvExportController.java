package com.example.registerjavafx;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import entity.ClassesEntity;
import entity.StudentsEntity;
import jakarta.persistence.Query;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.QueryException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CsvExportController implements Initializable {
    /* DATABASE
    (PK - primary key)
    listy eksporty:
    -lista_aktywności:
        +klasa PK, lista_studentów[], aktywność[] - odpowiadające elementy
    -lista_ocen
        +klasa PK, student[], oceny[][] - spróbować dwuwymiarową tablicę przez string
    -klasy DONE
        +classID PK, nazwa_grupy, studentIDList[] ("1,4,7, ..."), listaAktywności, lista_ocen (można rozważyć skondensowanie), max, (bez obecn. il. st.)
    -studenci DONE
        +studentID PK, imie, nazwisko, stan_studenta (String), rok_ur, ilosc_punktow

    TODO w wolnym czasie zidentyfikować miejsca, w których można zastąpić wyszukiwanie studenta przez ID - uprościć kod.
    * */
    @FXML
    TextField studentsField, classesField;
    @FXML
    Label errorMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void exportButtonHandle(ActionEvent event){
        String studentF = studentsField.getText();
        String classF = classesField.getText();
        if(studentsField.getText().isBlank() || classesField.getText().isBlank()){
            errorMessage.setText("Uzupełnij wszystkie nazwy plików.");
        }else{
            saveStudentsToCSV(studentF);
            saveClassToCsv(classF);
            errorMessage.setText("Operacja zakonczona.");
        }
    }

    public static void saveStudentsToCSV(String filename) {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.openSession();

        Query query = session.createQuery("FROM StudentsEntity");
        List<StudentsEntity> entities = query.getResultList();
        session.close();

        session = factory.openSession();
        File file;
        if(!filename.contains(".csv")) {
            file = new File(filename + ".csv");
        }else{
            file = new File(filename);
        }
        try {
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);

            String[] header = {"student_id", "name", "surname", "student_condition", "birth_year", "email", "password"};
            writer.writeNext(header);
            for(StudentsEntity sl : entities){
                String[] data = {
                        String.valueOf(sl.getStudentId()),
                        sl.getName(),
                        sl.getSurname(),
                        sl.getStudentCondition(),
                        String.valueOf(sl.getBirthYear()),
                        sl.getEmail(),
                        sl.getPassword()};
                writer.writeNext(data);
            }

            writer.close();

        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void saveClassToCsv(String filename){

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.openSession();

        Query query = session.createQuery("FROM ClassesEntity");
        List<ClassesEntity> entities = query.getResultList();
        session.close();

        session = factory.openSession();
        File file;
        if(!filename.contains(".csv")) {
            file = new File(filename + ".csv");
        }else{
            file = new File(filename);
        }
        try {
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);

            String[] header = {"class_id", "group_name", "max_number"};
            writer.writeNext(header);
            for(ClassesEntity cl : entities){
                String[] data = {String.valueOf(cl.getClassId()), cl.getGroupName(), String.valueOf(cl.getMaxNumber())};
                writer.writeNext(data);
            }

            writer.close();

        }catch(Exception e){
            System.out.println(e);
        }
    }

    @FXML
    public void cancelButtonHandle(ActionEvent e){
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
    }
}
