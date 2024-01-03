package pl.edu.agh.to.school.student;

import java.time.LocalDate;

public record StudentDto(String firstName, String lastName, LocalDate birthDate, String indexNumber) {
    static public StudentDto toStudentDto(Student student) {
        return new StudentDto(student.firstName, student.lastName, student.birthDate, student.indexNumber);
    }
}
