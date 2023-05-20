package com.example.registerjavafx;

import entity.ClassesEntity;
import entity.ClassesStudentsEntity;
import entity.ClassesStudentsID;
import entity.StudentsEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.*;
import java.net.URL;
import java.util.*;

class CellProp extends ListCell<ClassesEntity>{
    @Override
    public void updateItem(ClassesEntity item, boolean empty){
        super.updateItem(item, empty);
        if(item!=null){
            setText(item.getGroupName());
        }else{
            setText(null);
        }
    }
}
public class Controller implements Initializable{
    public StudentsEntity studentsEntity;
    public Set<ClassesEntity> studentClassesSet;
    @FXML
    private TableView<ClassesEntity> myClassList;
    @FXML
    private ComboBox<ClassesEntity> comboClasses;
    @FXML
    private ListView<String> classGrades;
    @FXML
    private TableColumn tableNameColumn;
    @FXML
    private Label mean, imieLabel, nazwiskoLabel, rokLabel, errorLabel;
    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources){

        Callback<ListView<ClassesEntity>, ListCell<ClassesEntity>> classesFactory = lv -> new ListCell<ClassesEntity>(){
            protected void updateItem(ClassesEntity ce, boolean empty){
                super.updateItem(ce, empty);
                setText(empty ? "": ce.getGroupName());
            }
        };
        Callback<ListView<StudentsEntity>, ListCell<StudentsEntity>> gradesFactory = lv -> new ListCell<StudentsEntity>(){
            protected void updateItem(StudentsEntity ce, boolean empty){
                super.updateItem(ce, empty);
                setText(empty ? "": ce.getName());
            }
        };

        comboClasses.setCellFactory(classesFactory);
        comboClasses.setButtonCell(new CellProp());

        comboClasses.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ClassesEntity>(){
            @Override
            public void changed(ObservableValue<? extends ClassesEntity> observable, ClassesEntity oldValue, ClassesEntity newValue) {
                classGrades.getItems().clear();
                ClassesEntity selectedClass = comboClasses.getValue();

                if(selectedClass!=null){
                    Configuration configuration = new Configuration();
                    configuration.configure("hibernate.cfg.xml");
                    SessionFactory factory = configuration.buildSessionFactory();
                    Session session = factory.openSession();
                    Transaction tx = session.beginTransaction();

                    if(selectedClass.equals("dowolny")){
                        for(ClassesEntity c: studentClassesSet){
                            ClassesStudentsEntity student_class_relation = session.get(ClassesStudentsEntity.class, new ClassesStudentsID(selectedClass.getClassId(), studentsEntity.getStudentId()));
                            Integer[] studentGrades = student_class_relation.getGrades();

                            if(studentGrades!=null || studentGrades.length!=0){
                                //System.out.println("Są oceny");
                                classGrades.getItems().add(Arrays.toString(studentGrades));
                            }else{
                                classGrades.getItems().add("Brak ocen");
                            }
                        }


                    }else {
                        ClassesStudentsEntity student_class_relation = session.get(ClassesStudentsEntity.class, new ClassesStudentsID(selectedClass.getClassId(), studentsEntity.getStudentId()));
                        Integer[] studentGrades = student_class_relation.getGrades();
                        if (studentGrades != null) {
                            classGrades.getItems().addAll(Arrays.toString(studentGrades));
                        }else{
                            classGrades.getItems().add("Brak ocen.");
                        }
                    }
                }
            }
        });
    }
    public void populate(){
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        Set<ClassesEntity> currentStudentClasses = session.get(StudentsEntity.class, studentsEntity.getStudentId()).getClassesSet();

        //inicjalizacja tabeli
        tableNameColumn = new TableColumn<ClassesEntity, String>("Nazwa grupy");
        tableNameColumn.setCellValueFactory(new PropertyValueFactory<Class, String>("groupName"));

        myClassList.getColumns().add(tableNameColumn);
        myClassList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        myClassList.getItems().addAll(currentStudentClasses);
        comboClasses.getItems().addAll(currentStudentClasses);
        comboClasses.getSelectionModel().select(0);

        tx.commit();
        session.close();
        refresh();
    }

    //NOT IN USE
//    @FXML
//    private void handleUpdateInfoButton(ActionEvent event) throws IOException {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("updateInfo_fxml"));
//        Parent root = loader.load();
//        UpdateController controller = loader.getController();
//        controller.currentStudent = currentStudent;
//        controller.SetOfClasses = SetOfClasses;
//        controller.SetOfStudents = SetOfStudents;
//
//        Scene scene = new Scene(root);
//        Stage stage = new Stage();
//        stage.setTitle("Twoje informacje");
//        stage.setScene(scene);
//        stage.show();
//    }
    @FXML
    private void handleSignInInfoButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("signInClass.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        SignInClassController controller = loader.getController();

        controller.studentsEntity = studentsEntity;
        controller.populateTable();


        Stage stage = new Stage();
        stage.setTitle("Wszystkie przedmioty");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void handleRefreshButton(ActionEvent event) throws IOException {
        refresh();
    }
    @FXML
    private void handleRemoveRequestButton(ActionEvent event) throws IOException {
        if(myClassList.getSelectionModel().getSelectedItem()!=null){

        ClassesEntity selectedClass = (ClassesEntity) myClassList.getSelectionModel().getSelectedItem();

        //refresh
        myClassList.getItems().clear();
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        ClassesStudentsEntity csRelation = session.get(ClassesStudentsEntity.class, new ClassesStudentsID(selectedClass.getClassId(), studentsEntity.getStudentId()));
        session.remove(csRelation);

        Set<ClassesEntity> currentStudentClasses = session.get(StudentsEntity.class, studentsEntity.getStudentId()).getClassesSet();
        tx.commit();
        session.close();
        myClassList.getItems().addAll(currentStudentClasses);

        comboClasses.getItems().remove(selectedClass);

        }else{
            errorLabel.setText("Zaznacz przedmiot.");
        }
    }
    public void refresh(){
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        Set<ClassesEntity> currentStudentClasses = session.get(StudentsEntity.class, studentsEntity.getStudentId()).getClassesSet();
        String[] currentStudentClassesNames = currentStudentClasses.stream().map(klasa -> klasa.getGroupName()).toArray(String[]::new);

        imieLabel.setText(studentsEntity.getName());
        nazwiskoLabel.setText(studentsEntity.getSurname());
        rokLabel.setText(String.valueOf(studentsEntity.getBirthYear()));
        myClassList.getItems().clear();
        myClassList.getItems().addAll(currentStudentClasses);
        comboClasses.getItems().clear();

        comboClasses.getItems().addAll(currentStudentClasses);
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
    public void onWindowClose() {
//        saveInFile(SetOfClasses, "SetOfClasses.ser");
    }

    @FXML
    public void logoutButtonHandle(ActionEvent e) throws IOException {
        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
        Controller controller = new Controller();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Parent loginRoot = fxmlLoader.load();
        LoginController loginController = fxmlLoader.getController();
        loginController.mainController = controller;


        Scene scene = new Scene(loginRoot);
        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}