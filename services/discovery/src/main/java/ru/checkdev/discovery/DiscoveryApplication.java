package ru.checkdev.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DiscoveryApplication.class);
        application.addListeners(new ApplicationPidFileWriter("./discovery.pid"));
        application.run();
    }
}
