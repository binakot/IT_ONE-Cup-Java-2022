package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingFilterConfig {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        final CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeHeaders(false);

        filter.setIncludeQueryString(true);

        filter.setIncludePayload(false);
        filter.setMaxPayloadLength(1024);

        filter.setBeforeMessagePrefix("");
        filter.setAfterMessagePrefix("");
        filter.setBeforeMessageSuffix("");
        filter.setAfterMessageSuffix("");

        return filter;
    }
}
