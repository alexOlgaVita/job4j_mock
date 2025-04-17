package ru.checkdev.mock.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.checkdev.mock.handler.RestTemplateResponseErrorHandler;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }
}
