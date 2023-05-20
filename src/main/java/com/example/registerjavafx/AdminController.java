package com.example.registerjavafx;

import entity.ClassesEntity;
import entity.StudentsEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.*;
import java.net.URL;
import java.util.*;
//TODO w wolnym czasie zaimplementować queries (akceptowanie i anulowanie wejścia do jakiejś klasy lub wyjścia z niej)
public class AdminController implements Initializable {
    //public ClassContainer SetOfClasses;
//    public List<Student> SetOfStudents;
    List<Class> allClasses;
//    public Set<Student> studentSet;
    String[] classes;
    @FXML
    private Button editClassButton, deleteClassButton, editStudentButton, deleteStudentButton, addGradeButton;
    @FXML
    private TableView classTable, studentTable;
    @FXML
    private TableColumn tableNameColumn, tableMaxColumn, tableStudentIdColumn, tableClassIdColumn, tableFirstNameColumn, tableSurnameColumn, tableEmailColumn, tablePasswordColumn;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Pobieramy z bazy danych wszystkich studentow
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        Session session = configuration.buildSessionFactory().openSession();
        List<StudentsEntity> allStudents = loadAllData(StudentsEntity.class, session);
        List<ClassesEntity> allClasses = loadAllData(ClassesEntity.class, session);
        session.close();
        //KONIEC


        //inicjalizacja tabeli classTable
        tableClassIdColumn = new TableColumn<ClassesEntity, Integer>("ID");
        tableClassIdColumn.setCellValueFactory(new PropertyValueFactory<ClassesEntity, Integer>("classId"));
        tableNameColumn = new TableColumn<ClassesEntity, String>("Nazwa grupy");
        tableNameColumn.setCellValueFactory(new PropertyValueFactory<ClassesEntity, String>("groupName"));
        TableColumn<ClassesEntity, Integer> tableNumberColumn = new TableColumn<>("No. studentów");
        tableNumberColumn.setCellValueFactory(prop -> new SimpleObjectProperty<>(prop.getValue().getStudentsNumber()));
        tableMaxColumn = new TableColumn<ClassesEntity, String>("Max");
        tableMaxColumn.setCellValueFactory(new PropertyValueFactory<ClassesEntity, String>("maxNumber"));

        classTable.getColumns().add(tableClassIdColumn);
        classTable.getColumns().add(tableNameColumn);
        classTable.getColumns().add(tableNumberColumn);
        classTable.getColumns().add(tableMaxColumn);
        classTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        classTable.getItems().addAll(allClasses);
        //koniec
        //inicjalizacja tabeli studentTable

        tableStudentIdColumn = new TableColumn<StudentsEntity, Integer>("ID");
        tableStudentIdColumn.setCellValueFactory(new PropertyValueFactory<StudentsEntity, Integer>("studentId"));
        tableFirstNameColumn = new TableColumn<StudentsEntity, String>("Imię");
        tableFirstNameColumn.setCellValueFactory(new PropertyValueFactory<StudentsEntity, String>("name"));
        tableSurnameColumn = new TableColumn<StudentsEntity, String>("Nazwisko");
        tableSurnameColumn.setCellValueFactory(new PropertyValueFactory<StudentsEntity, String>("surname"));
        tableEmailColumn = new TableColumn<StudentsEntity, String>("Email");
        tableEmailColumn.setCellValueFactory(new PropertyValueFactory<StudentsEntity, String>("email"));
        tablePasswordColumn = new TableColumn<StudentsEntity, String>("Haslo");
        tablePasswordColumn.setCellValueFactory(new PropertyValueFactory<StudentsEntity, String>("password"));

        studentTable.getColumns().add(tableStudentIdColumn);
        studentTable.getColumns().add(tableFirstNameColumn);
        studentTable.getColumns().add(tableSurnameColumn);
        studentTable.getColumns().add(tableEmailColumn);
        studentTable.getColumns().add(tablePasswordColumn);
        studentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        studentTable.getItems().addAll(allStudents.toArray());
        //koniec

        classTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                editClassButton.setDisable(false);
                deleteClassButton.setDisable(false);
            }
        });

        studentTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                editStudentButton.setDisable(false);
                deleteStudentButton.setDisable(false);
                addGradeButton.setDisable(false);
            }
        });
    }
    @FXML
    public void addClassButtonHandle(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addClass-panel.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        AddClassController controller = loader.getController();


        Stage stage = new Stage();
        stage.setTitle("Dodaj klasę");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void deleteClassButtonHandle(ActionEvent e) throws IOException {
        ClassesEntity klasaToRemove = (ClassesEntity) classTable.getSelectionModel().getSelectedItem();

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        ClassesEntity classesEntity = session.get(ClassesEntity.class, klasaToRemove.getClassId());

        if(classesEntity!=null){
            Set<StudentsEntity> studentsSet = classesEntity.getStudentsSet();
            for(StudentsEntity student : studentsSet){
                student.getClassesSet().remove(classesEntity);
            }

            classesEntity.getStudentsSet().clear();

            session.remove(classesEntity);
        }

        tx.commit();
        session.close();
    }
    @FXML
    public void editClassButtonHandle(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("updateClass-panel.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        UpdateClassController controller = loader.getController();

        ClassesEntity klasa = (ClassesEntity) classTable.getSelectionModel().getSelectedItem();
        controller.nameField.setText(klasa.getGroupName());
        controller.name = klasa.getGroupName();
        controller.maxField.setText(klasa.getMaxNumber().toString());
        controller.classID = klasa.getClassId();

        Stage stage = new Stage();
        stage.setTitle("Edytuj klasę");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void addStudentButtonHandle(ActionEvent e) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addStudent-panel.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        AddStudentController controller = loader.getController();
        Stage stage = new Stage();
        stage.setTitle("Dodaj studenta");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void editStudentButtonHandle(ActionEvent e) throws IOException{
//        Student student = (Student) studentTable.getSelectionModel().getSelectedItem();
        StudentsEntity student = (StudentsEntity) studentTable.getSelectionModel().getSelectedItem();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("editStudent-panel.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        EditStudentController controller = loader.getController();
        controller.studentsEntity = student;
        controller.nameField.setText(student.getName());
        controller.surnameField.setText(student.getSurname());
        controller.stateCombobox.setValue(StudentsEntity.StudentCondition.valueOf(student.getStudentCondition()));
        controller.yearField.setText(String.valueOf(student.getBirthYear()));
        controller.emailField.setText(student.getEmail());
        controller.passwordField.setText(student.getPassword());

        Stage stage = new Stage();
        stage.setTitle("Edytuj studenta");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void deleteStudentButtonHandle(ActionEvent e) throws IOException{
        StudentsEntity studentToRemove = (StudentsEntity) studentTable.getSelectionModel().getSelectedItem();

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        StudentsEntity studentsEntity = session.get(StudentsEntity.class, studentToRemove.getStudentId());

        if(studentsEntity!=null){
            Set<ClassesEntity> classesSet = studentsEntity.getClassesSet();

            for(ClassesEntity classesEntity : classesSet){
                classesEntity.getStudentsSet().remove(studentsEntity);
            }

            studentsEntity.getClassesSet().clear();

            session.remove(studentsEntity);
        }

        tx.commit();
        session.close();
    }
    @FXML
    public void addGradeButtonHandle(ActionEvent e) throws IOException{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addGrade-panel.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        AddGradeController controller = loader.getController();
        controller.studentsEntity = (StudentsEntity)studentTable.getSelectionModel().getSelectedItem(); //student z tabeli a nie z klasy

        controller.populateList();

        Stage stage = new Stage();
        stage.setTitle("Dodaj ocene");
        stage.setScene(scene);
        stage.show();
    }

    //todo OPCJONALNIE dodać widok ocen dla zaznaczonego studenta dla kazdego przedmiotu w którym jest

    @FXML
    private void handleRefreshButton(ActionEvent event) throws IOException {
        refresh();
    }

    private static <T> List<T> loadAllData(java.lang.Class<T> type, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        List<T> data = session.createQuery(criteria).getResultList();
        return data;
    }

    private void refresh(){
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        Session session = configuration.buildSessionFactory().openSession();
        List<StudentsEntity> allStudents = loadAllData(StudentsEntity.class, session);
        List<ClassesEntity> allClasses = loadAllData(ClassesEntity.class, session);
        session.close();

        studentTable.getItems().clear();
        classTable.getItems().clear();

        studentTable.getItems().addAll(allStudents);
        classTable.getItems().addAll(allClasses);
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
            System.out.println("Błąd podczas zapisania pliku " + filename);
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
            System.out.println("Błąd podczas otwierania pliku " + filename);
            System.out.println(e);
        }
        return null;
    }
    public void onWindowClose() {
//        saveInFile(SetOfClasses, "SetOfClasses.ser");
    }
    @FXML
    public void csvExportButtonHandle(ActionEvent e) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("csvExport-panel.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        CsvExportController controller = loader.getController();
//        controller.SetOfStudents = SetOfStudents;
//        controller.SetOfClasses = SetOfClasses;
//        controller.populate();

        Stage stage = new Stage();
        stage.setTitle("Eksport dziennika.");
        stage.setScene(scene);
        stage.show();
    }

//    Currently not in use
//    @FXML
//    public void csvImportButtonHandle(ActionEvent e) throws IOException {
//
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("csvImport-panel_fxml"));
//        Parent root = loader.load();
//        Scene scene = new Scene(root);
//        CsvImportController controller = loader.getController();
//        controller.SetOfStudents = SetOfStudents;
//        controller.SetOfClasses = SetOfClasses;
//        controller.populate();
//
//        Stage stage = new Stage();
//        stage.setTitle("Import dziennika.");
//        stage.setScene(scene);
//        stage.show();
//    }

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
    public List<ClassesEntity> getStudentClasses(int studentId) {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        try (Session session = configuration.buildSessionFactory().openSession();) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<ClassesEntity> query = builder.createQuery(ClassesEntity.class);
            Root<ClassesEntity> root = query.from(ClassesEntity.class);
            query.select(root)
                    .where(builder.equal(root.get("students").get("student_id"), studentId));
            return session.createQuery(query).getResultList();
        }
    }
    public List<StudentsEntity> getClassStudents(int studentId) {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        try (Session session = configuration.buildSessionFactory().openSession();) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<StudentsEntity> query = builder.createQuery(StudentsEntity.class);
            Root<StudentsEntity> root = query.from(StudentsEntity.class);
            query.select(root)
                    .where(builder.equal(root.get("classes").get("class_id"), studentId));
            return session.createQuery(query).getResultList();
        }
    }
}
