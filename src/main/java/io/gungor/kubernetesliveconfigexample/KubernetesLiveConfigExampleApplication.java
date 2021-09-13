package io.gungor.kubernetesliveconfigexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KubernetesLiveConfigExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(KubernetesLiveConfigExampleApplication.class, args);
    }
}
