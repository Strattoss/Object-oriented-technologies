package pl.edu.agh.to.school.student;

import pl.edu.agh.to.school.grade.Grade;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Student {
    @Id
    @GeneratedValue
    public int id;
    public String firstName;
    public String lastName;
    public LocalDate birthDate;
    public String indexNumber;
    @OneToMany
    public Set<Grade> grades = new HashSet<>();

    public Student(String firstName, String lastName, LocalDate birthDate, String indexNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.indexNumber = indexNumber;
    }

    public Student() {
    }

    public void giveGrade(Grade grade) {
        grades.add(grade);
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getIndexNumber() {
        return indexNumber;
    }
}
