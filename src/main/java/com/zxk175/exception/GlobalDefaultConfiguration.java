package com.zxk175.exception;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zxk175
 * @since 2020-03-20 17:11
 */
@Configuration
public class GlobalDefaultConfiguration {

    @Bean
    public GlobalExceptionHandler globalDefaultExceptionHandler() {
        return new GlobalExceptionHandler();
    }

}