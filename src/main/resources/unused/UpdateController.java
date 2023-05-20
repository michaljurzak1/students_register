//package com.example.registerjavafx;
//
//import entity.StudentsEntity;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.cfg.Configuration;
//
//import java.util.List;
//
//public class UpdateController {
//    //Student currentStudent;
//    List<Student> SetOfStudents;
//    public ClassContainer SetOfClasses;
//    @FXML
//    private TextField imieText, nazwiskoText, rokText;
//    @FXML
//    private Label errorMessage;
//    @FXML
//    private Button updateInfoButton;
//
//    public void handleUpdateInfoButton(ActionEvent event){
//        //TODO zamiana na update w bazie danych
//        try{
//            if(imieText.getText().trim().equals("") || nazwiskoText.getText().trim().equals("") || rokText.getText().trim().equals("") ||
//            imieText.getText()==null || nazwiskoText.getText()==null || rokText.getText()==null){
//                errorMessage.setText("Proszę uzupełnić pola.");
//            }else{
//                currentStudent.imie = imieText.getText();
//                currentStudent.nazwisko = nazwiskoText.getText();
//                currentStudent.rok_urodzenia = Integer.parseInt(rokText.getText());
//
//                outerloop:
//                for(Class c: SetOfClasses.grupy.values()){
//                    for(Student s: c.lista_studentow){
//                        if(currentStudent.imie.equals(s.imie) &&
//                                currentStudent.nazwisko.equals(s.nazwisko) &&
//                                currentStudent.stan_studenta.equals(s.stan_studenta) &&
//                                currentStudent.rok_urodzenia.equals(s.rok_urodzenia) &&
//                                currentStudent.email.equals(s.email) && currentStudent.password.equals(s.password)){
//
//                            s.imie = imieText.getText();
//                            s.nazwisko = nazwiskoText.getText();
//                            s.rok_urodzenia = Integer.parseInt(rokText.getText());
//
//                            Configuration configuration = new Configuration();
//                            configuration.configure("hibernate.cfg.xml");
//                            SessionFactory factory = configuration.buildSessionFactory();
//                            Session session = factory.openSession();
//                            Transaction tx = session.beginTransaction();
//
//                            StudentsEntity studentsEntity = session.get(StudentsEntity.class, s.studentID);
//                            studentsEntity.setName(imieText.getText());
//                            studentsEntity.setSurname(nazwiskoText.getText());
//                            studentsEntity.setBirthYear(Integer.parseInt(rokText.getText()));
//
//                            session.update(studentsEntity);
//                            tx.commit();
//                            session.close();
//                            break outerloop;
//                        }
//                    }
//                }
//                for(Student s: SetOfStudents){
//                    if(currentStudent.imie.equals(s.imie) &&
//                            currentStudent.nazwisko.equals(s.nazwisko) &&
//                            currentStudent.stan_studenta.equals(s.stan_studenta) &&
//                            currentStudent.rok_urodzenia.equals(s.rok_urodzenia) &&
//                            currentStudent.email.equals(s.email) && currentStudent.password.equals(s.password)){
//
//                        s.imie = imieText.getText();
//                        s.nazwisko = nazwiskoText.getText();
//                        s.rok_urodzenia = Integer.parseInt(rokText.getText());
//                        break;
//                    }
//                }
//
//
//                ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
//            }
//        }catch(NumberFormatException e){
//            errorMessage.setText("Podaj właściwy rok.");
//        }
//        catch (Exception e){
//            System.out.println(e);
//        }
//    }
//}
