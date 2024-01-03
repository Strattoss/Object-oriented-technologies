package pl.edu.agh.to.school;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.agh.to.school.course.Course;
import pl.edu.agh.to.school.course.CourseRepository;
import pl.edu.agh.to.school.grade.Grade;
import pl.edu.agh.to.school.grade.GradeRepository;
import pl.edu.agh.to.school.student.Student;
import pl.edu.agh.to.school.student.StudentRepository;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DatabaseConfigurator {
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository, CourseRepository courseRepository, GradeRepository gradeRepository) {
        return args -> {
            if (studentRepository.count() == 0 && courseRepository.count() == 0 && gradeRepository.count() == 0) {
                List<Student> students = List.of(
                        new Student("Jan", "Kowalski", LocalDate.now(), "123456"),
                        new Student("Jan", "Mal", LocalDate.now(), "111111"),
                        new Student("Kan", "Lan", LocalDate.now(), "222222"),
                        new Student("Marek", "Drzehuta", LocalDate.now(), "333333"),
                        new Student("Anna", "Panna", LocalDate.now(), "444444"),
                        new Student("Hildegarda", "du'Pont", LocalDate.now(), "555555"),
                        new Student("Ewa", "Nowak", LocalDate.now(), "666666"),
                        new Student("Adam", "Wójcik", LocalDate.now(), "777777")
                );

                List<Course> courses = List.of(
                        new Course("Matma"),
                        new Course("Fiza"),
                        new Course("Informatyka"),
                        new Course("Chemia")
                );

                courses.get(0).assignStudent(students.get(0));
                courses.get(0).assignStudent(students.get(2));
                courses.get(0).assignStudent(students.get(3));
                courses.get(0).assignStudent(students.get(5));
                courses.get(1).assignStudent(students.get(0));
                courses.get(1).assignStudent(students.get(1));
                courses.get(1).assignStudent(students.get(3));
                courses.get(2).assignStudent(students.get(0));
                courses.get(2).assignStudent(students.get(2));
                courses.get(2).assignStudent(students.get(4));
                courses.get(3).assignStudent(students.get(2));
                courses.get(3).assignStudent(students.get(3));
                courses.get(3).assignStudent(students.get(4));
                courses.get(3).assignStudent(students.get(6));

                List<Grade> grades = List.of(
                        new Grade(3, courses.get(0)),
                        new Grade(5, courses.get(0)),
                        new Grade(4, courses.get(1)),
                        new Grade(5, courses.get(2)),
                        new Grade(4, courses.get(2)),
                        new Grade(5, courses.get(3)),
                        new Grade(3, courses.get(3))
                );

                studentRepository.saveAll(students);

                students.get(2).giveGrade(grades.get(1));
                students.get(3).giveGrade(grades.get(0));
                students.get(1).giveGrade(grades.get(2));
                students.get(2).giveGrade(grades.get(3));
                students.get(4).giveGrade(grades.get(4));
                students.get(3).giveGrade(grades.get(5));
                students.get(2).giveGrade(grades.get(6));

                courseRepository.saveAll(courses);
                gradeRepository.saveAll(grades);

                studentRepository.saveAll(students);
            }
        };
    }
}
