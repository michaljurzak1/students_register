//package com.example.registerjavafx;
//
//import entity.StudentsEntity;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.net.URL;
//import java.util.*;
//
//public class CsvImportController implements Initializable {
//    @FXML
//    Label errorMessage, studentsLabel, classesLabel;
//    public ClassContainer SetOfClasses;
//    public List<Student> SetOfStudents;
//    public Set<Student> studentSet;
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        //studentSet = new HashSet<Student>();
//        //studentSet.addAll(SetOfStudents);   //possibly redundant
//    }
//
//    @FXML
//    public void importButtonHandle(ActionEvent event){
//        if(studentsLabel.getText().isBlank() || classesLabel.getText().isBlank()){
//            errorMessage.setText("Wybierz wszystkie pliki.");
//        }else{
//            try{
//                BufferedReader csvStudentReader = new BufferedReader(new FileReader(studentsLabel.getText()));
//                String row;
//                csvStudentReader.readLine();
//                SetOfStudents.clear();
//
//                while((row = csvStudentReader.readLine()) != null){
//                    String[] data = row.split(",");
//                    Integer studentID = Integer.parseInt(data[0]);
//                    String imie = data[1];
//                    String nazwisko = data[2];
//                    StudentsEntity.StudentCondition stan_studenta = StudentsEntity.StudentCondition.valueOf(data[3]);
//                    Integer rok_urodzenia = Integer.parseInt(data[4]);
//                    String email = data[5];
//                    String password = data[6];
//
//                    Student student = new Student(imie, nazwisko, stan_studenta, rok_urodzenia, email, password, studentID);
//                    SetOfStudents.add(student);
//                }
//
//                BufferedReader csvClassReader = new BufferedReader(new FileReader(classesLabel.getText()));
//                csvClassReader.readLine();
//                SetOfClasses.grupy.clear();
//
//                while((row = csvClassReader.readLine()) != null){
//                    String[] data = row.split(",");
//                    Integer classId = Integer.parseInt(data[0]);
//                    String nazwa_grupy = data[1];
//                    Integer max_ilosc_studentow = Integer.parseInt(data[4]);
//
//                    Class klasa = new Class(nazwa_grupy, max_ilosc_studentow, classId);
//                    //konwersja ze string na liste
//                    List<Student> lista_studentow = new ArrayList<>();
//                    String[] ls = data[2].split(";"); //id studentow
//                    List<Integer> listOfStudentIDs = new ArrayList<Integer>();
//                    for(int i=0; i<ls.length; i++){
//                        listOfStudentIDs.add(Integer.parseInt(ls[i]));
//                    }
//
//                    for(Student s: SetOfStudents){
//                        if(listOfStudentIDs.contains(s.studentID)){
//                            lista_studentow.add(s);
//                        }
//                    }
//                    klasa.lista_studentow = lista_studentow;
//                    klasa.obecna_ilosc_studentow = klasa.lista_studentow.size();
//                    //koniec konwersji - lista_studentow to wynik
//                    //pora na konwersje dla ocen:
//                    String[] lo = data[3].split("];");
//                    List<String[]> lo1 = new ArrayList<>();
//                    for(String s: lo){
//                        System.out.println(s);
//                        s = s.replace("[", "");
//                        s = s.replace("]", "");
//                        lo1.add(s.split(";"));
//                        System.out.println(s);
//                    }
//
//                    System.out.println(Arrays.toString(lo));
//                    for(int i = 0; i<lista_studentow.size(); i++){
//                        for(String s: lo1.get(i)){
//                            if(!s.equals("")) {
//                                Integer ocena = Integer.parseInt(s);
//                                //klasa.addGrade(lista_studentow.get(i), StudentsEntity.grade.fromInteger(ocena));
//                            }
//                        }
//                    }
//
////                    for(String s: lo){
////                        System.out.println(s);
////                        Integer ocena = Integer.parseInt(s);
////                        for(Student student: SetOfStudents){
////                            if(student.studentID.toString().equals(s) && ocena!=0){
////                                klasa.addGrade(student, Student.grade.fromInteger(ocena));
////                            }
////                        }
////                    }
//                    SetOfClasses.grupy.put(klasa.nazwa_grupy, klasa);
//
//                }
//
//
//                errorMessage.setText("Pomyślnie otwarto pliki.");
//            }catch(IOException e){
//                System.out.println("Wystąpił bład podczas wczytywania danych");
//            }
//        }
//    }
//    @FXML
//    public void openStudentsFromCSV(ActionEvent event) {
//        String studentsFileName;
//        FileChooser studentsChooser = new FileChooser();
//        File selectedStudentsFile = studentsChooser.showOpenDialog(null);
//        if(selectedStudentsFile != null){
//            studentsFileName = selectedStudentsFile.getName();
//            studentsLabel.setText(studentsFileName);
//        }
//    }
//    @FXML
//    public void openClassesFromCSV(ActionEvent event){
//        String classesFileName;
//        FileChooser classesChooser = new FileChooser();
//        File selectedClassesFile = classesChooser.showOpenDialog(null);
//        if(selectedClassesFile != null){
//            classesFileName = selectedClassesFile.getName();
//            classesLabel.setText(classesFileName);
//        }
//    }
//
//    @FXML
//    public void cancelButtonHandle(ActionEvent e){
//        ((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
//    }
//
//    public void populate(){
//        //studentSet.clear();
//        //studentSet.addAll(SetOfStudents);
//    }
//}
