package pl.edu.agh.school.persistence;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class SerializablePersistenceManagerBindingAnnotations {
    @Qualifier
    @Target({ FIELD, PARAMETER, METHOD })
    @Retention(RUNTIME)
    public @interface ClassStorageFileName {}

    @Qualifier
    @Target({ FIELD, PARAMETER, METHOD })
    @Retention(RUNTIME)
    public @interface TeacherStorageFileName {}
}
