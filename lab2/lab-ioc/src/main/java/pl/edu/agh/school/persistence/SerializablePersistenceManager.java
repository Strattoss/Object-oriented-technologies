package pl.edu.agh.school.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import pl.edu.agh.logger.Logger;
import pl.edu.agh.school.SchoolClass;
import pl.edu.agh.school.Teacher;
import pl.edu.agh.school.persistence.SerializablePersistenceManagerBindingAnnotations.*;

public final class SerializablePersistenceManager implements PersistenceManager {

    private final Logger log;

    private String teachersStorageFileName;

    private String classStorageFileName;

    @Inject
    public SerializablePersistenceManager(Logger logger) {
        log = logger;
        teachersStorageFileName = "teachers.dat";
        classStorageFileName = "classes.dat";
    }

    @Inject
    public void setTeachersStorageFileName(@TeacherStorageFileName String teachersStorageFileName) {
        this.teachersStorageFileName = teachersStorageFileName;
    }

    @Inject
    public void setClassStorageFileName(@ClassStorageFileName String classStorageFileName) {
        this.classStorageFileName = classStorageFileName;
    }

    @Override
    public void saveTeachers(List<Teacher> teachers) {
        if (teachers == null) {
            throw new IllegalArgumentException();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(teachersStorageFileName))) {
            log.log("Saving teachers...");
            oos.writeObject(teachers);
            log.log("Teachers saved successfully");
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            log.log("There was an error while saving the teachers data", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Teacher> loadTeachers() {
        ArrayList<Teacher> res = null;
        try (ObjectInputStream ios = new ObjectInputStream(new FileInputStream(teachersStorageFileName))) {
            log.log("Loading teachers...");
            res = (ArrayList<Teacher>) ios.readObject();
            log.log("Teachers loaded successfully");
        } catch (FileNotFoundException e) {
            res = new ArrayList<>();
        } catch (IOException e) {
            log.log("There was an error while loading the teachers data", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        return res;
    }

    @Override
    public void saveClasses(List<SchoolClass> classes) {
        if (classes == null) {
            throw new IllegalArgumentException();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(classStorageFileName))) {
            log.log("Saving classes...");
            oos.writeObject(classes);
            log.log("Classes saved successfully");
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            log.log("There was an error while saving the classes data", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SchoolClass> loadClasses() {
        ArrayList<SchoolClass> res = null;
        try (ObjectInputStream ios = new ObjectInputStream(new FileInputStream(classStorageFileName))) {
            log.log("Loading classes...");
            res = (ArrayList<SchoolClass>) ios.readObject();
            log.log("Classes loaded successfully");
        } catch (FileNotFoundException e) {
            res = new ArrayList<>();
        } catch (IOException e) {
            log.log("There was an error while loading the classes data", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        return res;
    }
}
