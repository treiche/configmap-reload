package io.treichenbach.configmapreload.bean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReloadBean {

    private final ReloadBeanConfig reloadBeanConfig;

    public ReloadBean(final ReloadBeanConfig reloadBeanConfig) {
        this.reloadBeanConfig = reloadBeanConfig;
        log.info("[{}] starting up", this.reloadBeanConfig);
    }

    public ReloadBeanConfig getConfig() {
        return this.reloadBeanConfig;
    }

    public static ReloadBean create(ReloadBeanConfig reloadBeanConfig) {
        return new ReloadBean(reloadBeanConfig);
    }
}
