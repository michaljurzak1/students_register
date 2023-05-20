//package com.example.registerjavafx;
//
//
//import entity.StudentsEntity;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Student implements Comparable<Student>, Serializable {
//    enum StudentCondition{
//        odrabiajacy,
//        chory,
//        nieobecny
//    }
//    public String imie;
//
//    public String getImie() {
//        return imie;
//    }
//
//    public void setImie(String imie) {
//        this.imie = imie;
//    }
//
//    public String getNazwisko() {
//        return nazwisko;
//    }
//
//    public void setNazwisko(String nazwisko) {
//        this.nazwisko = nazwisko;
//    }
//    public Integer studentID;
//
//    public String nazwisko;
//    StudentCondition stan_studenta;
//    Integer rok_urodzenia;
//    Double ilosc_punktow;
//    List<Integer> lista_ocen;
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String email;
//    String password;
//
////    List<Integer> addGrade(StudentsEntity.grade enumGrade){
////        Integer newGrade = enumGrade.getGrade();
////        lista_ocen.add(newGrade);
////        return lista_ocen;
////    }
//
//    Double classMean(){
//        Double sum = 0.0;
//        for(Integer i : lista_ocen){
//            sum += Double.valueOf(i);
//        }
//        return sum/lista_ocen.size();
//    }
//
//    Student(String i, String n, StudentCondition s_studenta, Integer r, String e, String p, Integer ID){
//        imie = i;
//        nazwisko=n;
//        stan_studenta=s_studenta;
//        rok_urodzenia=r;
//        email = e;
//        password = p;
//        lista_ocen = new ArrayList<Integer>();
//        //TODO implementacja inkrementacji studentId przy dodawaniu
//        studentID = ID;
//    }
//
//    void print(){
//        System.out.println(imie+" "+nazwisko+" "+stan_studenta+" rocznik: "+rok_urodzenia+" punkty: "+ilosc_punktow);
//    }
//
//    void updateStudent(String i, String n, StudentCondition s_studenta, Integer r){
//        imie = i;
//        nazwisko=n;
//        stan_studenta=s_studenta;
//        rok_urodzenia=r;
//    }
//
//    @Override
//    public int compareTo(Student o) {
//        int porownaneNazwiska = nazwisko.compareTo(o.nazwisko);
//
//        if(porownaneNazwiska == 0){
//            return imie.compareTo(o.imie);
//        } else{
//            return porownaneNazwiska;
//        }
//    }
//}
//
