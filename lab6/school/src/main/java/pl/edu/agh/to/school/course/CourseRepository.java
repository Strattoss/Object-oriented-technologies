package pl.edu.agh.to.school.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.agh.to.school.student.Student;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query("select c.students from Course c where c.id=:course_id")
    public List<Student> findStudents(@Param("course_id") int course_id);

    @Query("select avg(g.gradeValue) from Course c join c.students s join s.grades g where c.id=:courseId")
    public Optional<Double> getAvgCourseGrade(int courseId);
}
