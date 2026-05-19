package ru.yandex.practicum;

import java.io.IOException;
import java.util.logging.*;

public final class AppLogger {

    private static boolean initialized = false;

    private AppLogger() {
    }

    public static synchronized void init() {
        if (initialized)
            return;

        try {
            Logger rootLogger = Logger.getLogger("");

            for (Handler h : rootLogger.getHandlers()) {
                rootLogger.removeHandler(h);
            }

            FileHandler fileHandler = new FileHandler("app.log", 10 * 1024 * 1024, 5, true);

            fileHandler.setLevel(Level.INFO);
            fileHandler.setFormatter(new SimpleFormatter());

            rootLogger.addHandler(fileHandler);

            initialized = true;

        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize logging", e);
        }
    }

    public static Logger getLogger(Class<?> clazz) {
        init();
        return Logger.getLogger(clazz.getName());
    }
}