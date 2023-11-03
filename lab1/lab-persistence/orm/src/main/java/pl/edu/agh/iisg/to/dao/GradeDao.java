package pl.edu.agh.iisg.to.dao;

import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Grade;
import pl.edu.agh.iisg.to.model.Student;

import javax.persistence.PersistenceException;

public class GradeDao extends GenericDao<Grade> {

    public boolean gradeStudent(final Student student, final Course course, final float grade) {
        Grade gradeToAdd = new Grade(student, course, grade);
        
        student.gradeSet().add(gradeToAdd);
        course.gradeSet().add(gradeToAdd);

        try {
            save(gradeToAdd);
            return true;
        } catch (PersistenceException e) {
            e.printStackTrace();
            return false;
        }
    }


}
