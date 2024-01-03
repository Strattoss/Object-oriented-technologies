package pl.edu.agh.to.school.student;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.agh.to.school.course.CourseRepository;
import pl.edu.agh.to.school.grade.GradeRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
public class StudentServiceTest {
    @Mock
    public StudentRepository studentRepository;
    @Mock
    public CourseRepository courseRepository;
    @Mock
    public GradeRepository gradeRepository;

    @InjectMocks
    public StudentService studentService;

    @Test
    public void getNonemptyStudentsTest() {
        Student student1 = mock(Student.class);
        Student student2 = mock(Student.class);
        List<Student> students = List.of(student1, student2);

        when(studentRepository.findAll()).thenReturn(students);

        assertThat(studentService.getStudents().size()).isEqualTo(2);
        assertThat(students).contains(student1, student2);
    }

    @Test
    public void getEmptyStudentsTest() {
        List<Student> students = List.of();

        when(studentRepository.findAll()).thenReturn(students);

        assertThat(studentService.getStudents().size()).isEqualTo(0);
    }

    @Test
    public void getExistingStudentByIndexNumberTest() {
        Student student = mock(Student.class);
        String indexNumber = "123456";

        when(student.getIndexNumber()).thenReturn(indexNumber);
        when(studentRepository.findStudentByIndexNumber(indexNumber)).thenReturn(Optional.of(student));

        assertThat(studentService.getStudentByIndexNumber(indexNumber)).isEqualTo(Optional.of(student));
    }

    @Test
    public void getNonexistingStudentByIndexNumberTest() {
        String indexNumber = "123456";

        when(studentRepository.findStudentByIndexNumber(indexNumber)).thenReturn(Optional.empty());

        assertThat(studentService.getStudentByIndexNumber(indexNumber)).isEqualTo(Optional.empty());
    }


}
