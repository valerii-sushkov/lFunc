package helpersLocal;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LogHandler {
    private static LambdaLogger logger;
    private String loggerName;
    private static List<String> resLines = new ArrayList<>();
    private static String screenShot;

    public LogHandler(final String name) {
        loggerName = name;
    }

    public static void init(final Context context) {
        logger = context.getLogger();
    }

    public void debug(final String message) {
        log(Level.FINE, message);
    }

    public void info(final String message) {
        log(Level.INFO, message);
    }

    public void warn(final String message) {
        log(Level.WARNING, message);
    }

    public void severe(final String message) {
        log(Level.SEVERE, message);
    }

    public void severe(final String message, final Throwable exception) {
        log(Level.SEVERE, message, exception);
    }

    public void externalServiceLog(final String message) {
        String text = formatLine("LAMBDA", message);
        lambdaLog(text);
        resLines.add("[LLog]" + text);
    }

    public void lambdaLog(final String text) {
        if (logger != null) {
            logger.log(text);
        }
    }

    private void log(final Level level, final String message) {
        String text = formatLine(level.getName(), message);
        if (level.intValue() > 800) {
            lambdaLog(text);
        }
        if (logger == null) {
            Logger.getLogger(loggerName).log(level, text);
        }
        resLines.add(text);
    }

    private void log(final Level level, final String message, final Throwable exception) {
        String text = formatLine(level.getName(), message + "\n"
                + exception.toString() + "\n" +
                Arrays.stream(exception.getStackTrace())
                        .limit(10).map(elem -> elem.toString())
                        .collect(Collectors.joining("\n")));
        if (level.intValue() > 800) {
            lambdaLog(text);
        }
        if (logger == null) {
            Logger.getLogger(loggerName).log(level, message, exception);
        }
        resLines.add(text);
    }

    private String formatLine(final String prefix, final String text) {
        return "[" + prefix + "] [" + loggerName + "] " + text;
    }

    public static List<String> extractLog() {
        return resLines;
    }

    public static String getScreenShot() {
        return screenShot;
    }

    public void setScreenShot(String screenShot) {
        this.screenShot = screenShot;
    }
}
