package pl.edu.agh.logger;

import com.google.inject.Singleton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class Logger {
    protected static Logger logger;

    protected DateFormat dateFormat;

    protected Set<IMessageSerializer> registeredSerializers;

    public Logger() {
        init();
        this.registeredSerializers = new HashSet<IMessageSerializer>();
        logger = this;
    }

    public Logger(Set<IMessageSerializer> registeredSerializers) {
        init();
        if (registeredSerializers == null) {
            throw new IllegalArgumentException("null argument");
        }
        this.registeredSerializers = registeredSerializers;
        logger = this;
    }

    public static Logger getInstance() {
        if (logger == null)
            throw new RuntimeException("Logger hasn't been initialised with any Guice injector");
        return logger;
    }

    public void registerSerializer(IMessageSerializer messageSerializer) {
        registeredSerializers.add(messageSerializer);
    }

    public void log(String message) {
        log(message, null);
    }

    public void log(String message, Throwable error) {
        for (IMessageSerializer messageSerializer : registeredSerializers) {
            String formattedMessage = dateFormat.format(new Date())
                    + ": " + message + (error != null ? error.toString() : "");
            messageSerializer.serializeMessage(formattedMessage);
        }
    }

    private void init() {
        dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    }

}
