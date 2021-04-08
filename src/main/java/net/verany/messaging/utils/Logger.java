package net.verany.messaging.utils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;

public class Logger {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public Logger(String message) {
        System.out.println(MessageFormat.format("[{0}] {1}", dateFormat.format(System.currentTimeMillis()), message));
    }
}
