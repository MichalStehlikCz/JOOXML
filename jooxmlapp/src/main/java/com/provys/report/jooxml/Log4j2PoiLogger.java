package com.provys.report.jooxml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.apache.poi.util.POILogger.*;

/**
 * POI Logger implementation, redirecting calls to Log4j2
 */
public class Log4j2PoiLogger /*extends POILogger*/ {

    private Logger logger;

    //@Override
    public void initialize(final String cat) {
        this.logger = LogManager.getLogger(cat);
    }

    /**
         * Log a message
         *
         * @param level One of DEBUG, INFO, WARN, ERROR, FATAL
         * @param object The object to log.
         */
    //@Override
    protected void _log(final int level, final Object object) {
        switch (level) {
            case FATAL:
                logger.fatal(object);
                break;
            case ERROR:
                logger.error(object);
                break;
            case WARN:
                logger.warn(object);
                break;
            case INFO:
                logger.info(object);
                break;
            case DEBUG:
                logger.debug(object);
                break;
            case 0:
                logger.trace(object);
                break;
        }
    }

    /**
     * Log a message
     *
     * @param level     One of DEBUG, INFO, WARN, ERROR, FATAL
     * @param object      The object to log.  This is converted to a string.
     * @param exception An exception to be logged
     */
    //@Override
    protected void _log(final int level, final Object object, final Throwable exception) {
        switch (level) {
            case FATAL:
                logger.fatal(object, exception);
                break;
            case ERROR:
                logger.error(object, exception);
                break;
            case WARN:
                logger.warn(object, exception);
                break;
            case INFO:
                logger.info(object, exception);
                break;
            case DEBUG:
                logger.debug(object, exception);
                break;
            case 0:
                logger.trace(object, exception);
                break;
        }
    }

    /**
     * Check if a logger is enabled to log at the specified level
     *
     * @param level One of DEBUG, INFO, WARN, ERROR, FATAL
     */
    //@Override
    public boolean check(final int level) {
        switch (level) {
            case FATAL:
                return logger.isDebugEnabled();
            case ERROR:
                return logger.isErrorEnabled();
            case WARN:
                return logger.isWarnEnabled();
            case INFO:
                return logger.isInfoEnabled();
            case DEBUG:
                return logger.isDebugEnabled();
            case 0:
                return logger.isTraceEnabled();
        }
        return false;
    }

}
