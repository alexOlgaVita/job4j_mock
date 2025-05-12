package ru.checkdev.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { FreeMarkerAutoConfiguration.class })
public class AuthSrv {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AuthSrv.class);
        application.addListeners(new ApplicationPidFileWriter("./auth.pid"));
        application.run();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
