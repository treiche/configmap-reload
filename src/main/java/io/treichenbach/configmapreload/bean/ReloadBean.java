package io.treichenbach.configmapreload.bean;

import io.treichenbach.configmapreload.config.ReloadBeanProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReloadBean {

    private final ReloadBeanProperties reloadBeanProperties;

    public ReloadBean(final ReloadBeanProperties reloadBeanProperties) {
        log.info("[{}] starting up", reloadBeanProperties);
        this.reloadBeanProperties = reloadBeanProperties;
    }

    public String getValue() {
        log.info(reloadBeanProperties.toString());
        return reloadBeanProperties.getValue();
    }
}