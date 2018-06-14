package com.seclore.sample.dms.util;

import java.io.File;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LoggerUtil {
    private static final String SAMPLE_APP_LOGGER = "SampleAppLogger";

    private static Logger logger = null;

    public static void initializeLogger(String appPath) {
        logger = Logger.getLogger(SAMPLE_APP_LOGGER);
        if (logger == null) {
            return;
        }

        FileAppender appender = (FileAppender) logger.getAppender(SAMPLE_APP_LOGGER);
        if (appender == null) {
            return;
        }

        String logFile = appender.getFile();
        if (logFile == null || logFile.trim().isEmpty()) {
            logFile = "logs/SecloreSampleApp.log";
        }
        appender.setFile(appPath + File.separator + logFile);
        appender.setEncoding("UTF-8");
        appender.activateOptions();

        logger.info("LoggerUtil::initializeLogger:: SampleAppLogger initialized succesfully.");

    }

    public static void logInfo(String pMessage) {
        if (logger != null) {
            logger.info(pMessage);
        }
    }

    public static void logDebug(String pMessage) {
        if (logger != null) {
            logger.debug(pMessage);
        }
    }

    public static void logError(String pMessage) {
        if (logger != null) {
            logger.error(pMessage);
        }
    }

    public static void logError(String pMessage, Throwable pThrowable) {
        if (logger != null) {
            logger.error(pMessage, pThrowable);
        }
    }

    /**
     * Set logger level, at run time if required
     *
     * @param pLevel
     */
    public static void setLoggerLevel(String pLevel) {
        if (logger == null) {
            return;
        }

        if ("all".equalsIgnoreCase(pLevel)) {
            logger.setLevel(Level.ALL);
        } else if ("debug".equalsIgnoreCase(pLevel)) {
            logger.setLevel(Level.DEBUG);
        } else if ("error".equalsIgnoreCase(pLevel)) {
            logger.setLevel(Level.ERROR);
        } else if ("fatal".equalsIgnoreCase(pLevel)) {
            logger.setLevel(Level.FATAL);
        } else if ("info".equalsIgnoreCase(pLevel)) {
            logger.setLevel(Level.INFO);
        } else if ("off".equalsIgnoreCase(pLevel)) {
            logger.setLevel(Level.OFF);
        } else if ("trace".equalsIgnoreCase(pLevel)) {
            logger.setLevel(Level.TRACE);
        } else if ("warn".equalsIgnoreCase(pLevel)) {
            logger.setLevel(Level.WARN);
        }
    }

}
