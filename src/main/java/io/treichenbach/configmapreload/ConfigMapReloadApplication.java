package io.treichenbach.configmapreload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ConfigMapReloadApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigMapReloadApplication.class, args);
    }
}
