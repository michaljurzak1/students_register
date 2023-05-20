package entity;

//import com.example.registerjavafx.Student;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "students", schema = "public", catalog = "postgres")
public class StudentsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "student_id")
    private Integer studentId;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "surname")
    private String surname;
    @Basic
    @Column(name = "student_condition")
    private String studentCondition;
    @Basic
    @Column(name = "birth_year")
    private Integer birthYear;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "password")
    private String password;

    public Set<ClassesEntity> getClassesSet() {
        return ClassesSet;
    }

    public void setClassesSet(Set<ClassesEntity> classesSet) {
        ClassesSet = classesSet;
    }

    public enum StudentCondition{
        odrabiajacy,
        chory,
        nieobecny
    }

    public enum grade{
        niedostateczny(1),
        dopuszczajacy(2),
        dostateczny(3),
        dobry(4),
        bardzo_dobry(5),
        celujacy(6);
        private Integer grade;
        public Integer getGrade(){
            return this.grade;
        }

        public StudentsEntity.grade fromInteger(Integer num){
            for(StudentsEntity.grade g : StudentsEntity.grade.values()){
                if(g.getGrade().equals(num)){
                    return g;
                }
            }
            System.out.println("Integer: "+ num);
            return null;
        }

        public StudentsEntity.grade fromString(String text){
            for(StudentsEntity.grade g : StudentsEntity.grade.values()){
                if(g.toString().equals(text)){
                    return g;
                }
            }
            System.out.println("text: "+ text);
            return null;
        }

        private grade(Integer grade){
            this.grade = grade;
        }
    }

    @ManyToMany(targetEntity = ClassesEntity.class, fetch = FetchType.EAGER)
    @JoinTable(
            name= "classes_students",
            joinColumns=@JoinColumn(name= "student_id"),
            inverseJoinColumns = @JoinColumn(name= "class_id")
    )
    private Set<ClassesEntity> ClassesSet;

    public StudentsEntity findByEmail(String email) {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<StudentsEntity> query = builder.createQuery(StudentsEntity.class);
            Root<StudentsEntity> root = query.from(StudentsEntity.class);
            query.select(root).where(builder.equal(root.get("email"), email));
            return session.createQuery(query).uniqueResultOptional().orElse(null);
        }
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getStudentCondition() {
        return this.studentCondition;
    }

    public void setStudentCondition(String studentCondition) {
        this.studentCondition = studentCondition;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentsEntity that = (StudentsEntity) o;
        return Objects.equals(studentId, that.studentId) && Objects.equals(name, that.name) && Objects.equals(surname, that.surname) && Objects.equals(studentCondition, that.studentCondition) && Objects.equals(birthYear, that.birthYear) && Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, name, surname, studentCondition, birthYear, email, password);
    }
}
