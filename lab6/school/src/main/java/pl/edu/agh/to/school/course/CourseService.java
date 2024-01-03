package pl.edu.agh.to.school.course;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.school.student.Student;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    public List<Student> getCourseStudents(int course_id) {
        return courseRepository.findStudents(course_id);
    }

    public Optional<Double> calculateCourseAvgGrade(int courseId) {
        return courseRepository.getAvgCourseGrade(courseId);
    }
}
