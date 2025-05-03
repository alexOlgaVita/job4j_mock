package ru.checkdev.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = { FreeMarkerAutoConfiguration.class })
public class NtfSrv {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(NtfSrv.class);
        application.addListeners(new ApplicationPidFileWriter("./notification.pid"));
        application.run();
    }
}
