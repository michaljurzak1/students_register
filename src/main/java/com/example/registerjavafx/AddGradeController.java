package com.example.registerjavafx;

import entity.ClassesEntity;
import entity.ClassesStudentsEntity;
import entity.ClassesStudentsID;
import entity.StudentsEntity;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.*;
import java.net.URL;
import java.util.*;

public class AddGradeController implements Initializable {
    public StudentsEntity studentsEntity;
    public Set<ClassesEntity> studentClassesSet;

    @FXML
    ComboBox<ClassesEntity> classesCombobox;
    @FXML
    ComboBox<StudentsEntity.grade> gradesCombobox;
    @FXML
    ListView gradesList;
    @FXML
    Label errorMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Callback<ListView<ClassesEntity>, ListCell<ClassesEntity>> factory = lv -> new ListCell<ClassesEntity>(){
            protected void updateItem(ClassesEntity ce, boolean empty){
                super.updateItem(ce, empty);
                setText(empty ? "": ce.getGroupName());
            }
        };

        classesCombobox.setCellFactory(factory);
        classesCombobox.setButtonCell(new CellProp());
        gradesCombobox.getItems().addAll(StudentsEntity.grade.values());

        classesCombobox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ClassesEntity>(){
            @Override
            public void changed(ObservableValue<? extends ClassesEntity> observable, ClassesEntity oldValue, ClassesEntity newValue) {

                gradesList.getItems().clear();

                ClassesEntity selectedClass = classesCombobox.getSelectionModel().getSelectedItem();

                if(studentClassesSet==null){
                    System.out.println("Student nie ma ocen.");
                }else if(studentClassesSet.contains(selectedClass)){
                    Configuration configuration = new Configuration();
                    configuration.configure("hibernate.cfg.xml");
                    SessionFactory factory = configuration.buildSessionFactory();
                    Session session = factory.openSession();

                    ClassesStudentsEntity student_class_relation = session.get(ClassesStudentsEntity.class, new ClassesStudentsID(selectedClass.getClassId(), studentsEntity.getStudentId()));
                    Integer[] studentGrades = student_class_relation.getGrades();

                    if(studentGrades==null || studentGrades.length==0){
                        gradesList.getItems().add("Brak ocen");

                    }else{
                        gradesList.getItems().addAll(Arrays.stream(studentGrades).toList());
                    }
                    session.close();
                }
            }
        });
    }
    public void populateList(){
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        studentClassesSet = session.get(StudentsEntity.class, studentsEntity.getStudentId()).getClassesSet();
        classesCombobox.getItems().addAll(studentClassesSet);
    }

    @FXML
    public void addGradeButtonHandle(ActionEvent e) {

            ClassesEntity selectedClass = classesCombobox.getSelectionModel().getSelectedItem();

            StudentsEntity.grade grade = gradesCombobox.getSelectionModel().getSelectedItem();
            System.out.println(grade.getGrade());

            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            SessionFactory factory = configuration.buildSessionFactory();
            Session session = factory.openSession();
            Transaction tx = session.beginTransaction();

            String sqlQuery = "UPDATE classes_students SET grades = array_append(grades, :value) WHERE class_id=:c AND student_id=:s";
            session.createNativeQuery(sqlQuery).setParameter("value", grade.getGrade())
                            .setParameter("c", selectedClass.getClassId())
                            .setParameter("s", studentsEntity.getStudentId())
                            .executeUpdate();

            gradesList.getItems().clear();
            ClassesStudentsEntity student_class_relation = session.get(ClassesStudentsEntity.class, new ClassesStudentsID(selectedClass.getClassId(), studentsEntity.getStudentId()));
            Integer[] studentGrades = student_class_relation.getGrades();
            if(studentGrades==null || studentGrades.length==0){
                gradesList.getItems().add("Brak ocen");

            }else{
                gradesList.getItems().addAll(Arrays.stream(studentGrades).toList());
            }

            tx.commit();
            session.close();
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
