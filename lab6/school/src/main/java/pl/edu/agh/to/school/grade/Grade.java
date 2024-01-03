package pl.edu.agh.to.school.grade;

import pl.edu.agh.to.school.course.Course;

import javax.persistence.*;

@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public int gradeValue;
    @OneToOne
    public Course course;

    public Grade(int gradeValue, Course course) {
        this.gradeValue = gradeValue;
        this.course = course;
    }

    public Grade() {
    }

    public int getId() {
        return id;
    }

    public int getGradeValue() {
        return gradeValue;
    }

    public Course getCourse() {
        return course;
    }
}
