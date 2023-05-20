package com.example.registerjavafx;

import entity.ClassesEntity;
import entity.ClassesStudentsEntity;
import entity.ClassesStudentsID;
import entity.StudentsEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.hibernate.Internal;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SignInClassController implements Initializable {
    StudentsEntity studentsEntity;
    @FXML
    TextField searchBar;
    @FXML
    Button sendRequestButton;
    @FXML
    public Label responseLabel;
    @FXML
    private TableView allClassesTable;
    @FXML
    private TableColumn tableNameColumn, tableLimitColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableNameColumn = new TableColumn<ClassesEntity, String>("Nazwa grupy");
        tableNameColumn.setCellValueFactory(new PropertyValueFactory<ClassesEntity, String>("groupName"));
        tableNameColumn.setMinWidth(80);

        TableColumn<ClassesEntity, Integer> tableCurrentColumn = new TableColumn<>("Obecna ilosc");
        tableCurrentColumn.setCellValueFactory(prop -> new SimpleObjectProperty<>(prop.getValue().getStudentsNumber()));

        tableLimitColumn = new TableColumn<ClassesEntity, Integer>("Limit");
        tableLimitColumn.setCellValueFactory(new PropertyValueFactory<ClassesEntity, Integer>("maxNumber"));


        allClassesTable.getColumns().add(tableNameColumn);
        allClassesTable.getColumns().add(tableCurrentColumn);
        allClassesTable.getColumns().add(tableLimitColumn);
        allClassesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);



        allClassesTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ClassesEntity>() {
            @Override
            public void changed(ObservableValue<? extends ClassesEntity> observable, ClassesEntity oldValue, ClassesEntity newValue) {
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");
                SessionFactory factory = configuration.buildSessionFactory();
                Session session = factory.openSession();
                Transaction tx = session.beginTransaction();

                ClassesEntity currentClass = (ClassesEntity)allClassesTable.getSelectionModel().getSelectedItem();
                Set<ClassesEntity> currentStudentClasses = session.get(StudentsEntity.class, studentsEntity.getStudentId()).getClassesSet();
                if(currentStudentClasses.contains(currentClass)){
                    sendRequestButton.setDisable(true);
                }else{
                    sendRequestButton.setDisable(false);
                }
                if(currentClass.getStudentsNumber()>=currentClass.getMaxNumber()){
                    sendRequestButton.setDisable(true);
                }
                tx.commit();
                session.close();
            }

        });
        searchBar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                FilteredList<ClassesEntity> filteredData = new FilteredList<>(allClassesTable.getItems(), b->true);
                searchBar.textProperty().addListener((observable1, oldValue1, newValue1) -> {
                    filteredData.setPredicate(klasa ->{
                        if(newValue1 == null|| newValue1.isEmpty()){
                            return true;
                        }
                        String lowerCaseFilter = newValue1.toLowerCase();
                        if(klasa.getGroupName().toLowerCase().indexOf(lowerCaseFilter) != -1){
                            return true;
                        } else if(String.valueOf(klasa.getStudentsNumber()).indexOf(lowerCaseFilter) != -1){
                            return true;
                        } else if(String.valueOf(klasa.getMaxNumber()).indexOf(lowerCaseFilter) != -1){
                            return true;
                        } else{
                            return false;
                        }
                    });
                });
                SortedList<ClassesEntity> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(allClassesTable.comparatorProperty());
                allClassesTable.setItems(sortedData);
            }
        });

    }

    public void populateTable(){
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            List<ClassesEntity> allClasses = loadAllData(ClassesEntity.class, session);
            allClassesTable.getItems().addAll(allClasses);
        }catch(Exception e){
            e.printStackTrace();
        }
        tx.commit();
        session.close();
    }

    @FXML
    public void handleSignInRequest(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("main-panel.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();

        try{
            ClassesEntity klasa = (ClassesEntity) allClassesTable.getSelectionModel().getSelectedItem();

            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            Session session = configuration.buildSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            ClassesStudentsEntity csRelation = new ClassesStudentsEntity();
            csRelation.classesStudentsID = new ClassesStudentsID(klasa.getClassId(), studentsEntity.getStudentId());
            Integer[] grades = null;
            csRelation.grades = (Integer[])grades;
            String sql = "INSERT INTO classes_students (class_id, student_id) VALUES (:c, :s)";
            session.createNativeQuery(sql).setParameter("c", klasa.getClassId()).setParameter("s", studentsEntity.getStudentId()).executeUpdate();

            responseLabel.setText("Pomyślnie zapisano.");

            tx.commit();
            session.close();
        }catch(NullPointerException e){
            responseLabel.setText("Proszę zaznaczyć przedmiot.");
        }

    }
    @FXML
    public void handleExitSignInButton(ActionEvent event){
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    private static <T> List<T> loadAllData(java.lang.Class<T> type, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        List<T> data = session.createQuery(criteria).getResultList();
        return data;
    }
}
