package pl.edu.agh.iisg.to;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.edu.agh.iisg.to.dao.CourseDao;
import pl.edu.agh.iisg.to.dao.GradeDao;
import pl.edu.agh.iisg.to.dao.StudentDao;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Grade;
import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.session.SessionService;

import static org.junit.jupiter.api.Assertions.*;

public class OrmTest {

    private final StudentDao studentDao = new StudentDao();

    private final CourseDao courseDao = new CourseDao();

    private final GradeDao gradeDao = new GradeDao();

    @BeforeEach
    public void before() {
        SessionService.openSession();
    }

    @AfterEach
    public void after() {
        SessionService.closeSession();
    }

    @Test
    public void createStudentTest() {
        // When
        var student1 = studentDao.create("Adam", "Kowalski", 100122);
        var student2 = studentDao.create("Jan", "Nowak", 100123);
        var redundantStudent = studentDao.create("Kasia", "Kowalska", 100123);

        // Then
        checkStudent(student1);
        checkStudent(student2);

        assertNotEquals(student1.get().id(), student2.get().id());
        assertTrue(redundantStudent.isEmpty());
    }


    @Test
    public void findStudentIndexTest() {
        // When
        var student = studentDao.create("Kasia", "Kowalska", 300124);
        var foundStudent = studentDao.findByIndexNumber(student.get().indexNumber());

        // Then
        checkStudent(student);
        Assertions.assertEquals(student.get(), foundStudent.get());
    }

    @Test
    public void createCourseTest() {
        // When
        var course1 = courseDao.create("TO");
        var course2 = courseDao.create("TO2");
        var redundantCourse = courseDao.create("TO2");

        // Then
        checkCourse(course1);
        checkCourse(course2);

        assertNotEquals(course1.get().id(), course2.get().id());
        assertFalse(redundantCourse.isPresent());
    }

    @Test
    public void findCourseTest() {
        // When
        var course = courseDao.create("TK");
        var foundCourse = courseDao.findById(course.get().id());

        // Then
        checkCourse(course);
        assertEquals(course.get(), foundCourse.get());
    }

    @Test
    public void enrollStudentTest() {
        // When
        var student = studentDao.create("Kasia", "Kowalska", 700124);
        var course = courseDao.create("MOWNIT");

        boolean studentEnrolled = courseDao.enrollStudent(course.get(), student.get());
        boolean reundantStudentEnroll = courseDao.enrollStudent(course.get(), student.get());

        var courseStudents = course.get().studentSet();
        var studentCourses = student.get().courseSet();

        // Then
        checkStudent(student);
        checkCourse(course);

        assertTrue(studentEnrolled);
        assertFalse(reundantStudentEnroll);

        assertTrue(courseStudents.contains(student.get()));
        assertTrue(studentCourses.contains(course.get()));
    }

    @Test
    public void courseStudentListTest() {
        // When
        var student1 = studentDao.create("Adam", "Paciaciak", 800125);
        var student2 = studentDao.create("Jan", "Paciaciak", 800126);
        var course = courseDao.create("WDI");

        courseDao.enrollStudent(course.get(), student1.get());
        courseDao.enrollStudent(course.get(), student2.get());

        var students = course.get().studentSet();

        // Then
        checkStudent(student1);
        checkStudent(student2);
        checkCourse(course);

        assertEquals(2, students.size());
        assertTrue(students.contains(student1.get()));
        assertTrue(students.contains(student2.get()));
    }

    @Test
    public void gradeStudentTest() {
        // When
        var student = studentDao.create("Kasia", "Kowalska", 900124);
        var course = courseDao.create("MOWNIT 2");

        var initialStudentGradesSize = student.get().gradeSet().size();
        boolean studentGraded = gradeDao.gradeStudent(student.get(), course.get(), 5.0f);
        var resultStudentGradesSize = student.get().gradeSet().size();

        // Then
        checkStudent(student);
        checkCourse(course);

        assertTrue(studentGraded);
        assertEquals(0, initialStudentGradesSize);
        assertEquals(1, resultStudentGradesSize);
    }

    @Test
    public void createReportTest() {
        // When
        var student = studentDao.create("Kasia", "Kowalska", 1000124);
        var course1 = courseDao.create("Bazy");
        var course2 = courseDao.create("Bazy 2");

        gradeDao.gradeStudent(student.get(), course1.get(), 5.0f);
        gradeDao.gradeStudent(student.get(), course1.get(), 4.0f);
        gradeDao.gradeStudent(student.get(), course2.get(), 5.0f);
        gradeDao.gradeStudent(student.get(), course2.get(), 3.0f);

        Map<Course, Float> report = studentDao.createReport(student.get());

        // Then
        checkStudent(student);
        checkCourse(course1);
        checkCourse(course2);

        assertEquals(Float.compare(4.5f, report.get(course1.get())), 0);
        assertEquals(Float.compare(4.0f, report.get(course2.get())), 0);
    }

    private void checkStudent(final Optional<Student> student) {
        assertTrue(student.isPresent());
        student.ifPresent(s -> {
            assertTrue(s.id() > 0);
            Assertions.assertNotNull(s.firstName());
            Assertions.assertNotNull(s.lastName());
            assertTrue(s.indexNumber() > 0);
        });
    }

    private void checkCourse(final Optional<Course> course) {
        assertTrue(course.isPresent());
        course.ifPresent(c -> {
            assertTrue(c.id() > 0);
            Assertions.assertNotNull(c.name());
        });
    }

}
