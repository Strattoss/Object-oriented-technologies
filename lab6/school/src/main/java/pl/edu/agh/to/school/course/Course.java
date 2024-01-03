package pl.edu.agh.to.school.course;

import pl.edu.agh.to.school.student.Student;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Course {
    @Id
    @GeneratedValue
    public int id;
    public String name;
    @ManyToMany
    public Set<Student> students = new HashSet<>();

    public Course(String name) {
        this.name = name;
    }

    public Course() {
    }

    public void assignStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Student> getStudents() {
        return students;
    }
}
