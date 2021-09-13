package io.gungor.kubernetesliveconfigexample.controller;

import io.gungor.kubernetesliveconfigexample.bean.ReloadBean;
import io.gungor.kubernetesliveconfigexample.config.SpringConfigProperties;
import io.gungor.kubernetesliveconfigexample.service.EntityAvailability;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class ConfigController {

    private final SpringConfigProperties springConfigProperties;
    private final ReloadBean reloadBean;

    public ConfigController(SpringConfigProperties springConfigProperties, final ReloadBean reloadBean) {
        this.springConfigProperties = springConfigProperties;
        this.reloadBean = reloadBean;
    }

    @Autowired
    private EntityAvailability entityAvailability;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/envconfig")
    public List<String> getPropertyValue() {
        return entityAvailability.getUnavailableEntities();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/springconfig")
    public List<String> reloadConfig() {
        log.info("");
        log.info("config.value:" + springConfigProperties.getValue());
        return List.of("Value: " + springConfigProperties.getValue());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/beanconfig")
    public List<String> beanConfig() {
        log.info("");
        log.info("reload-bean.value:" + reloadBean.getValue());
        return List.of("Value: " + reloadBean.getValue());
    }
}
