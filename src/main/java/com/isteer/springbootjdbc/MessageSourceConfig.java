package com.isteer.springbootjdbc;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration //is used to declare a class as a source of bean definitions and provide application configuration.

// @Component -> @Service, @Repository, or @Controller, which are specializations of @Component
// It is used to indicate that a class is a candidate for auto-detection and auto-configuration as a bean
public class MessageSourceConfig {

    @Bean
    MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}