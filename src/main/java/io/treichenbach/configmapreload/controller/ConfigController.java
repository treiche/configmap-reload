package io.treichenbach.configmapreload.controller;

import io.treichenbach.configmapreload.bean.ReloadBeanConfig;
import io.treichenbach.configmapreload.bean.ReloadBean;
import io.treichenbach.configmapreload.config.SpringConfigProperties;
import io.treichenbach.configmapreload.service.EntityAvailabilityUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@RestController
@Slf4j
public class ConfigController {

    private final SpringConfigProperties springConfigProperties;
    private final ReloadBean reloadBean;
    private final ConfigurableApplicationContext context;
    private final EntityAvailabilityUpdater entityAvailabilityUpdater;

    public ConfigController(final SpringConfigProperties springConfigProperties, final ReloadBean reloadBean,
                            final ConfigurableApplicationContext context, final EntityAvailabilityUpdater entityAvailabilityUpdater) {
        this.springConfigProperties = springConfigProperties;
        this.reloadBean = reloadBean;
        this.context = context;
        this.entityAvailabilityUpdater = entityAvailabilityUpdater;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/envconfig")
    public List<String> getPropertyValue() {
        return entityAvailabilityUpdater.getUnavailableEntities();
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
    public ReloadBeanConfig beanConfig() {
        log.info("");
        log.info("bean config: {}", reloadBean.getConfig());
        return reloadBean.getConfig();
    }

    @RequestMapping("/override-config")
    @ResponseBody
    public Map<String, String> getOverrideConfigs() {
        return context.getEnvironment().getPropertySources().stream()
                      .filter(ps -> ps.getName().contains("application-override"))
                      .findFirst()
                      .map(ps -> ((Map<?, ?>) ps.getSource()).entrySet().stream()
                                                             .filter(entry -> !entry.getKey().equals("dummy"))
                                                             .collect(toMap(e -> e.getKey().toString(), e -> e.getValue().toString())))
                      .orElse(Map.of());
    }
}
