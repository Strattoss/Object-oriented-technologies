package pl.edu.agh.iisg.to;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import pl.edu.agh.iisg.to.connection.ConnectionProvider;
import pl.edu.agh.iisg.to.executor.QueryExecutor;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Grade;
import pl.edu.agh.iisg.to.model.Student;

import static org.junit.jupiter.api.Assertions.*;

public class ActiveRecordTest {

    @BeforeAll
    public static void init() {
        ConnectionProvider.init("jdbc:sqlite:active_record_test.db");
    }

    @BeforeEach
    public void setUp() throws SQLException {
        QueryExecutor.delete("DELETE FROM STUDENT_COURSE");
        QueryExecutor.delete("DELETE FROM STUDENT");
        QueryExecutor.delete("DELETE FROM COURSE");
        QueryExecutor.delete("DELETE FROM GRADE");
    }

    @AfterAll
    public static void cleanUp() throws SQLException {
        ConnectionProvider.close();
    }

    @Test
    public void createStudentTest() {
        // When
        var student1 = Student.create("Adam", "Kowalski", 100122);
        var student2 = Student.create("Jan", "Nowak", 100123);
        var redundantStudent = Student.create("Kasia", "Kowalska", 100123);

        // Then
        checkStudent(student1);
        checkStudent(student2);

        assertNotEquals(student1.get().id(), student2.get().id());
        assertFalse(redundantStudent.isPresent());
    }

    @Test
    public void findStudentTest() {
        // When
        var student = Student.create("Kasia", "Kowalska", 200124);
        var foundStudent = Student.findById(student.get().id());
        var nonExistingStudent = Student.findById(Integer.MAX_VALUE);

        // Then
        checkStudent(foundStudent);
        Assertions.assertEquals(student.get(), foundStudent.get());
        Assertions.assertFalse(nonExistingStudent.isPresent());
    }

    @Test
    public void findStudentIndexTest() {
        // When
        var student = Student.create("Kasia", "Kowalska", 300124);
        var foundStudent = Student.findByIndexNumber(student.get().indexNumber());

        // Then
        checkStudent(student);
        Assertions.assertEquals(student.get(), foundStudent.get());
    }

    @Test
    public void createCourseTest() {
        // When
        var course1 = Course.create("TO");
        var course2 = Course.create("TO2");
        var redundantCourse = Course.create("TO2");

        // Then
        checkCourse(course1);
        checkCourse(course2);

        assertNotEquals(course1.get().id(), course2.get().id());
        assertFalse(redundantCourse.isPresent());
    }

    @Test
    public void findCourseTest() {
        // When
        var course = Course.create("TK");
        var foundCourse = Course.findById(course.get().id());

        // Then
        checkCourse(course);
        assertEquals(course.get(), foundCourse.get());
    }

    @Test
    public void enrollStudentTest() {
        // When
        var student = Student.create("Kasia", "Kowalska", 700124);
        var course = Course.create("MOWNIT");

        boolean studentEnrolled = course.get().enrollStudent(student.get());
        boolean redundantStudentEnroll = course.get().enrollStudent(student.get());

        // Then
        checkStudent(student);
        checkCourse(course);

        assertTrue(studentEnrolled);
        assertFalse(redundantStudentEnroll);
    }

    @Test
    public void courseStudentListTest() {
        // When
        var student1 = Student.create("Adam", "Paciaciak", 800125);
        var student2 = Student.create("Jan", "Paciaciak", 800126);
        var course = Course.create("WDI");

        course.get().enrollStudent(student1.get());
        course.get().enrollStudent(student2.get());

        var students = course.get().studentList();

        // Then
        checkStudent(student1);
        checkStudent(student2);
        checkCourse(course);

        assertEquals(2, students.size());
        assertTrue(students.contains(student1.get()));
        assertTrue(students.contains(student2.get()));
    }

    @Test
    public void cachedCourseStudentListTest() {
        // When
        var student1 = Student.create("Adam", "Paciaciak", 800125);
        var student2 = Student.create("Jan", "Paciaciak", 800126);
        var course = Course.create("WDI");

        course.get().enrollStudent(student1.get());
        course.get().enrollStudent(student2.get());

        List<Student> students = course.get().cachedStudentsList();
        List<Student> cachedStudents = course.get().cachedStudentsList();

        // Then
        checkStudent(student1);
        checkStudent(student2);
        checkCourse(course);

        assertEquals(2, students.size());
        assertTrue(students.contains(student1.get()));
        assertTrue(students.contains(student2.get()));

        assertEquals(cachedStudents, students);
    }

    @Test
    public void gradeStudentTest() {
        // When
        var student = Student.create("Kasia", "Kowalska", 900124);
        var course = Course.create("MOWNIT 2");

        boolean studentGraded = Grade.gradeStudent(student.get(), course.get(), 5.0f);

        // Then
        checkStudent(student);
        checkCourse(course);

        assertTrue(studentGraded);
    }

    @Test
    public void createReportTest() {
        // When
        var student = Student.create("Kasia", "Kowalska", 1000124);
        var course1 = Course.create("Bazy");
        var course2 = Course.create("Bazy 2");

        Grade.gradeStudent(student.get(), course1.get(), 5.0f);
        Grade.gradeStudent(student.get(), course1.get(), 4.0f);
        Grade.gradeStudent(student.get(), course2.get(), 5.0f);
        Grade.gradeStudent(student.get(), course2.get(), 3.0f);

        Map<Course, Float> report = student.get().createReport();

        // Then
        checkStudent(student);
        checkCourse(course1);
        checkCourse(course2);

//        for (var entrySet : report.entrySet()) {
//            System.out.println("CourseId = " + entrySet.getKey().id() + ", grade = " + entrySet.getValue());
//        }

        assertEquals(Float.compare(4.5f, report.get(course1.get())), 0);
        assertEquals(Float.compare(4.0f, report.get(course2.get())), 0);
    }

    private void checkStudent(final Optional<Student> student) {
        assertTrue(student.isPresent());
        student.ifPresent(s -> {
            assertTrue(s.id() > 0);
            assertNotNull(s.firstName());
            assertNotNull(s.lastName());
            assertTrue(s.indexNumber() > 0);
        });
    }

    private void checkCourse(final Optional<Course> course) {
        assertTrue(course.isPresent());
        course.ifPresent(c -> {
            assertTrue(c.id() > 0);
            assertNotNull(c.name());
        });
    }

}
