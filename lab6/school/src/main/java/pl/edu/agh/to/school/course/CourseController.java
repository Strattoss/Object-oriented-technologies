package pl.edu.agh.to.school.course;

import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to.school.student.StudentDto;
import pl.edu.agh.to.school.student.StudentService;

import java.util.List;

@RestController
@RequestMapping(path = "courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseDto> getCourses() {
        return courseService.getCourses()
                .stream()
                .map(CourseDto::toCourseDto)
                .toList();
    }

    @GetMapping("{id}")
    public List<StudentDto> getCourse(@PathVariable int id) {
        return courseService.getCourseStudents(id)
                .stream()
                .map(StudentDto::toStudentDto)
                .toList();
    }

    @GetMapping("avgGrade")
    public Double getCourseAvgGrade(@RequestParam int courseId) {
        return courseService.calculateCourseAvgGrade(courseId).orElse(0.0);
    }
}
