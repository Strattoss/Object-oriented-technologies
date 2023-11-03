package pl.edu.agh.school;

import java.util.Collections;
import java.util.List;

import com.google.inject.Inject;
import pl.edu.agh.logger.Logger;
import pl.edu.agh.school.persistence.PersistenceManager;

public class SchoolDAO {

    public final Logger log;

    private final List<Teacher> teachers;

    private final List<SchoolClass> classes;

    private final PersistenceManager persistenceManager;

    @Inject
    public SchoolDAO(PersistenceManager persistenceManager, Logger logger) {
        log = logger;
        this.persistenceManager = persistenceManager;
        teachers = persistenceManager.loadTeachers();
        classes = persistenceManager.loadClasses();
    }

    public void addTeacher(Teacher teacher) {
        if (!teachers.contains(teacher)) {
            teachers.add(teacher);
            persistenceManager.saveTeachers(teachers);
            log.log("Added " + teacher.toString());
        }
    }

    public void addClass(SchoolClass newClass) {
        if (!classes.contains(newClass)) {
            classes.add(newClass);
            persistenceManager.saveClasses(classes);
            log.log("Added " + newClass.toString());
        }
    }

    public List<SchoolClass> getClasses() {
        return Collections.unmodifiableList(classes);
    }

    public List<Teacher> getTeachers() {
        return Collections.unmodifiableList(teachers);
    }
}
