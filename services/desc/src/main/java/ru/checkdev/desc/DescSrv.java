package ru.checkdev.desc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { FreeMarkerAutoConfiguration.class })
public class DescSrv {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DescSrv.class);
        application.addListeners(new ApplicationPidFileWriter("./desc.pid"));
        application.run();
    }
}

