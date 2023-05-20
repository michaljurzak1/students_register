module com.example.registerjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;


    opens com.example.registerjavafx to javafx.fxml;
    opens entity;
    exports com.example.registerjavafx;
    exports entity;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires com.opencsv;
}