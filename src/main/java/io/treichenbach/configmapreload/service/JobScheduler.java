package io.treichenbach.configmapreload.service;

import io.treichenbach.configmapreload.config.SpringConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class JobScheduler {

    @Autowired
    private SpringConfigProperties springConfigProperties;

    @Scheduled(fixedDelay = 1000)
    private void refresh() {

        log.info("#############################################");
        log.info(springConfigProperties.getBeanvalue());
        log.info(springConfigProperties.getValue());
        log.info("#############################################");

    }

}
