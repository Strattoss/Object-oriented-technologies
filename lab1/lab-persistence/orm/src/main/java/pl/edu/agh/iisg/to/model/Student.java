package pl.edu.agh.iisg.to.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = Student.TABLE_NAME)
public class Student {

    public static final String TABLE_NAME = "student";

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = Columns.ID)
    private int id;

    @Column(name = Columns.FIRST_NAME, nullable = false, length = 50)
    private String firstName;

    @Column(name = Columns.LAST_NAME, nullable = false, length = 50)
    private String lastName;

    @Column(name = Columns.INDEX_NUMBER, nullable = false, unique = true)
    private int indexNumber;

    @OneToMany(mappedBy = "student")
    private Set<Grade> gradeSet = new HashSet<>();

    @ManyToMany(mappedBy = "studentSet")
    private Set<Course> courseSet = new HashSet<>();

    Student() {
    }

    public Student(final String firstName, final String lastName, final int indexNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
    }

    public int id() {
        return id;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public int indexNumber() {
        return indexNumber;
    }

    public Set<Course> courseSet() {
        return courseSet;
    }

    public Set<Grade> gradeSet() {
        return gradeSet;
    }

    public static class Columns {

        public static final String ID = "id";

        public static final String FIRST_NAME = "first_name";

        public static final String LAST_NAME = "last_name";

        public static final String INDEX_NUMBER = "index_number";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != student.id) return false;
        if (indexNumber != student.indexNumber) return false;
        if (!firstName.equals(student.firstName)) return false;
        return lastName.equals(student.lastName);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + indexNumber;
        return result;
    }
}
