package org.cviggo.vbungee.shared;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Logger {

    private final java.util.logging.Logger uLogger;
    private final String logFolder;
    private AtomicInteger currentSevereCnt = new AtomicInteger();

    public Logger(java.util.logging.Logger uLogger, String logFolder) {
        this.uLogger = uLogger;
        this.logFolder = logFolder;
    }

    public void logInfo(String message) {
        uLogger.info(String.format("[%d] %s", Thread.currentThread().getId(), message));
    }

    public void logWarn(String message) {
        uLogger.warning(String.format("[%d] %s", Thread.currentThread().getId(), message));
    }

    public void logSevere(String message) {

        uLogger.severe(String.format("[%d] %s", Thread.currentThread().getId(), message));

        final int maxSevereLogsToDump = 10; // could consider settings, but nah!

        if (currentSevereCnt.incrementAndGet() < maxSevereLogsToDump) {

            final String dumpLogFilename = String.format("%d.dump.log", new Date().getTime());
            final String dumpLogFilePath = logFolder + File.separator + dumpLogFilename;

            try {
                PrintWriter out = new PrintWriter(dumpLogFilePath);
                out.println(String.format("[%d] %s", Thread.currentThread().getId(), message));
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void logSevere(String message, Throwable t) {
        String str = message + ". StackTrace: " + ExceptionUtils.getStackTrace(t);
        logSevere(str);
    }

    public void logSevere(Throwable t) {
        // http://stackoverflow.com/questions/1149703/stacktrace-to-string-in-java
        String str = ExceptionUtils.getStackTrace(t);
        logSevere(str);
    }
}
