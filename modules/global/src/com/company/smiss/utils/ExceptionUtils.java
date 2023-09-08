package com.company.smiss.utils;


import org.slf4j.Logger;

public class ExceptionUtils {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ExceptionUtils.class);

    public static String getStackTrace(Exception ex) {
        StringBuilder result = new StringBuilder();
        result.append(ex.toString()).append("\n");
        for (StackTraceElement element : ex.getStackTrace()) {
            result.append(element.toString()).append("\n");
        }
        return result.toString();
    }

    public static String logErrors(Exception ex) {
        StackTraceElement stackTraceElement = ex.getStackTrace()[0];
        String error = getStackTrace(ex);
        log.error(error);
        return error;
    }

    public static String logErrors(String message, Exception ex) {
        String error = message + "\n" + getStackTrace(ex);
        log.error(error);
        return error;
    }

}
