package pl.edu.agh.to.school.student;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import pl.edu.agh.to.school.course.Course;
import pl.edu.agh.to.school.course.CourseRepository;
import pl.edu.agh.to.school.grade.Grade;
import pl.edu.agh.to.school.grade.GradeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final GradeRepository gradeRepository;

    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository, GradeRepository gradeRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.gradeRepository = gradeRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentByIndexNumber(String indexNumber) {
        return studentRepository.findStudentByIndexNumber(indexNumber);
    }

    @Transactional
    public void addStudentGrade(String studentIndexNumber, int gradeValue, int courseId) {
        Optional<Student> student = studentRepository.findStudentByIndexNumber(studentIndexNumber);
        Optional<Course> course = courseRepository.findById(courseId);


        if (student.isPresent() && course.isPresent()) {
            // check if student is enrolled into the course
            if (!course.get().getStudents().contains(student.get())) {
                return;
            }

            // check if grade already exists; if so, overwrite it
            Optional<Grade> foundGrade = student.get().grades
                    .stream()
                    .filter(grade -> grade.course.getId() == courseId).findFirst();

            if (foundGrade.isPresent()) {
                foundGrade.get().gradeValue = gradeValue;
                gradeRepository.save(foundGrade.get());
                return;
            }

            // add the new grade
            Grade grade = new Grade(gradeValue, course.get());
            student.get().giveGrade(grade);

            gradeRepository.save(grade);
            studentRepository.save(student.get());
        }
    }

    public Optional<Double> calculateStudentGradeAverage(int studentId) {
        return studentRepository.getAverageGradeById(studentId);
    }
}