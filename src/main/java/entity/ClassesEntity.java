package entity;

import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "classes", schema = "public", catalog = "postgres")
public class ClassesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "class_id")
    private Integer classId;
    @Basic
    @Column(name = "group_name")
    private String groupName;
    @Basic
    @Column(name = "max_number")
    private Integer maxNumber;

    public Set<StudentsEntity> getStudentsSet() {
        return StudentsSet;
    }

    public Integer getStudentsNumber(){
        return getStudentsSet().size();
    }

    public void setStudentsSet(Set<StudentsEntity> studentsSet) {
        StudentsSet = studentsSet;
    }


    @ManyToMany(targetEntity = StudentsEntity.class, fetch = FetchType.EAGER)
    @JoinTable(
            name= "classes_students",
            joinColumns=@JoinColumn(name= "class_id"),
            inverseJoinColumns = @JoinColumn(name= "student_id")
    )
    private Set<StudentsEntity> StudentsSet;

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(Integer maxNumber) {
        this.maxNumber = maxNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassesEntity that = (ClassesEntity) o;
        return Objects.equals(classId, that.classId) && Objects.equals(groupName, that.groupName) && Objects.equals(maxNumber, that.maxNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classId, groupName, maxNumber);
    }
}
