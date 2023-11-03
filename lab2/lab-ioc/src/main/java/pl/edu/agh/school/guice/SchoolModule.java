package pl.edu.agh.school.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import pl.edu.agh.school.persistence.PersistenceManager;
import pl.edu.agh.school.persistence.SerializablePersistenceManager;
import pl.edu.agh.school.persistence.SerializablePersistenceManagerBindingAnnotations.*;

public class SchoolModule extends AbstractModule {
    @Override
    protected void configure() {
//        bindConstant()
//                .annotatedWith(Names.named("Teacher Storage File Name"))
//                .to("guice-teachers.dat");
//        bindConstant()
//                .annotatedWith(Names.named("Class Storage File Name"))
//                .to("guice-classes.dat");

        bindConstant()
                .annotatedWith(TeacherStorageFileName.class)
                .to("guice2-teachers.dat");
        bindConstant()
                .annotatedWith(ClassStorageFileName.class)
                .to("guice2-classes.dat");
    }

    @Provides
    PersistenceManager providePersistenceManager(SerializablePersistenceManager impl) {
        return impl;
    }
}
