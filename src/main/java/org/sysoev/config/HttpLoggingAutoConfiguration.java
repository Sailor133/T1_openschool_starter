package org.sysoev.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sysoev.aspect.LoggingAspect;
import org.sysoev.aspect.loging_lvl_service.LoggingLevelService;

@Configuration
@EnableConfigurationProperties(HttpLoggingProperties.class)
public class HttpLoggingAutoConfiguration {

    private final HttpLoggingProperties httpLoggingProperties;

    public HttpLoggingAutoConfiguration(HttpLoggingProperties httpLoggingProperties) {
        this.httpLoggingProperties = httpLoggingProperties;
    }

    @Bean
    public LoggingLevelService loggingLevelService() {
        return new LoggingLevelService(httpLoggingProperties);
    }

    @Bean
    public LoggingAspect loggingAspect(LoggingLevelService levelService) {
        if (httpLoggingProperties.isEnabled()) {
            return new LoggingAspect(levelService);
        } else {
            return null;
        }
    }
}