package pl.edu.agh.iisg.to.model;

import javax.persistence.*;

@Entity
@Table(name = Grade.TABLE_NAME)
public class Grade {

    public static final String TABLE_NAME = "grade";

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = Columns.ID)
    private int id;

    @Column(name = Columns.GRADE, nullable = false)
    private float grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Columns.STUDENT_ID)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Columns.COURSE_ID)
    private Course course;

    Grade() {
    }

    public Grade(final Student student, final Course course, final float grade) {
        this.student = student;
        this.course = course;
        this.grade = grade;
    }

    public int id() {
        return id;
    }

    public float grade() {
        return grade;
    }

    public Student student() {
        return student;
    }

    public Course course() {
        return course;
    }

    public static class Columns {

        public static final String ID = "id";

        public static final String GRADE = "grade";

        public static final String STUDENT_ID = "student_id";

        public static final String COURSE_ID = "course_id";

    }

}
