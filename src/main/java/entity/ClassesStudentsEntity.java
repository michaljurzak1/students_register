package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

@Entity(name="Classes_Students")
@Table(name = "classes_students", schema = "public", catalog = "postgres")
public class ClassesStudentsEntity {
    @Column(name = "grades", columnDefinition = "integer[]")
    public Object grades;

    public Integer[] getGrades() {
        System.out.println(grades);
        return (Integer[])grades;
    }

    public void setGrades(Object grades) {
        this.grades = grades;
    }
    @EmbeddedId
    public ClassesStudentsID classesStudentsID;

}
