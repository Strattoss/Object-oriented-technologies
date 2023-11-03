package pl.edu.agh.school.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.agh.logger.Logger;

public class SchoolModuleTest {
    private Injector injector = Guice.createInjector(new SchoolModule());

    @Test
    void testLoggerIsInstantiatedOnlyOnce () {
        Assertions.assertSame(injector.getInstance(Logger.class), injector.getInstance(Logger.class));
    }

}
