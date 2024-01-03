package pl.edu.agh.to.school.course;

import pl.edu.agh.to.school.student.Student;
import pl.edu.agh.to.school.student.StudentDto;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class CourseDto {
    public String name;
    public Set<StudentDto> students;

    public CourseDto(String name, Set<StudentDto> students) {
        this.name = name;
        this.students = students;
    }

    static public CourseDto toCourseDto(Course course) {
        Set<StudentDto> studentDtosSet = new HashSet<>();

        Stream<StudentDto> studentDtoStream = course.students.stream().map(StudentDto::toStudentDto);
        studentDtoStream.forEach(studentDtosSet::add);

        return new CourseDto(course.name, studentDtosSet);
    }
}
