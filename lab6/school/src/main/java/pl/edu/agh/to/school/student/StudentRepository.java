package pl.edu.agh.to.school.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    public Optional<Student> findStudentByIndexNumber(String indexNumber);

    @Query("SELECT avg(g.gradeValue) from Student s join s.grades g where s.id = :studentId")
    public Optional<Double> getAverageGradeById(int studentId);
}
