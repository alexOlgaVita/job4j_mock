package ru.checkdev.cloudconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServer {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ConfigServer.class);
        application.addListeners(new ApplicationPidFileWriter("./cloudconfig.pid"));
        application.run();
    }
}
