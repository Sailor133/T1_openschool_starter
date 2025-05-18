package org.sysoev.aspect.loging_lvl_service;

import org.slf4j.Logger;
import org.sysoev.config.HttpLoggingProperties;

public class LoggingLevelService {
    private final HttpLoggingProperties props;

    public LoggingLevelService(HttpLoggingProperties props) {
        this.props = props;
    }
    public void log(Logger log, String message) {
        switch (props.getLevel().toUpperCase()) {
            case "DEBUG" -> log.debug(message);
            case "WARN" -> log.warn(message);
            case "ERROR" -> log.error(message);
            default -> log.info(message);
        }
    }
}
