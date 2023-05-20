package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;

import java.io.Serializable;

@Embeddable
public class ClassesStudentsID implements Serializable {
    private Integer class_id;

    public Integer getClassID() {
        return class_id;
    }

    public void setClassID(Integer classID) {
        this.class_id = classID;
    }

    public Integer getStudentID() {
        return student_id;
    }

    public void setStudentID(Integer studentID) {
        this.student_id = studentID;
    }
    private Integer student_id;

    public ClassesStudentsID(Integer classId, Integer studentId) {
        this.class_id = classId;
        this.student_id = studentId;
    }

    public ClassesStudentsID() {}

}
