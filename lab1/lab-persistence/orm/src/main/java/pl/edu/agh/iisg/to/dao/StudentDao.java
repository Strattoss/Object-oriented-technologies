package pl.edu.agh.iisg.to.dao;

import java.util.*;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Student;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

public class StudentDao extends GenericDao<Student> {

    public Optional<Student> create(final String firstName, final String lastName, final int indexNumber) {
        try {
            Student savedStudent = save(new Student(firstName, lastName, indexNumber));
            return Optional.of(savedStudent);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Student> findByIndexNumber(final int indexNumber) {
        String sqlStatement = "SELECT s FROM Student s WHERE s.indexNumber = :indexNumber";

        try (Session currentSession = currentSession()) {
            Query query = currentSession.createQuery(sqlStatement);
            query.setParameter("indexNumber", indexNumber);

            Student foundStudent = (Student) query.getSingleResult();

            return Optional.of(foundStudent);
        } catch (NoResultException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Map<Course, Float> createReport(final Student student) {
        Query sqlQuery = currentSession().createQuery("SELECT g.course, AVG(g.grade) FROM Grade g WHERE g.student.id=:student_id GROUP BY g.course.id");
        sqlQuery.setParameter("student_id", student.id());
        try {
            List<Object[]> resultList = sqlQuery.getResultList();

            Map<Course, Float> report = new HashMap<>();

            for (var row : resultList) {
                report.put((Course) row[0], ((Double)row[1]).floatValue());
            }

            return report;
        } catch (PersistenceException e) {
            e.printStackTrace();
            return new HashMap<>();
        }

    }

}
