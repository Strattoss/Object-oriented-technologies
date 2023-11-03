package pl.edu.agh.school.persistence;

import pl.edu.agh.school.SchoolClass;
import pl.edu.agh.school.Teacher;

import java.util.List;

public interface PersistenceManager {
    void saveTeachers(List<Teacher> teachers);

    @SuppressWarnings("unchecked")
    List<Teacher> loadTeachers();

    void saveClasses(List<SchoolClass> classes);

    @SuppressWarnings("unchecked")
    List<SchoolClass> loadClasses();
}
