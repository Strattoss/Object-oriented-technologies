package pl.edu.agh.to.school.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.school.course.CourseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentDto> getStudents(@RequestParam(value = "indexNumber", required = false) String indexNumber) {
        if (indexNumber == null) {
            return studentService.getStudents()
                    .stream()
                    .map(StudentDto::toStudentDto)
                    .toList();
        }

        Optional<Student> student = studentService.getStudentByIndexNumber(indexNumber);

        return student.map(StudentDto::toStudentDto).map(List::of).orElse(new ArrayList<>());
    }


    @PutMapping
    public void addGrade(
            @RequestParam String studentIndexNumber,
            @RequestParam int gradeValue,
            @RequestParam int courseId) {
        studentService.addStudentGrade(studentIndexNumber, gradeValue, courseId);
    }

    @GetMapping("avgGrade")
    public Double calculateAverageGradeById(@RequestParam int studentId) {
        return studentService.calculateStudentGradeAverage(studentId).orElse(null);
    }
}