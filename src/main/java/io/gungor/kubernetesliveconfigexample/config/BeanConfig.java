package io.gungor.kubernetesliveconfigexample.config;

import io.gungor.kubernetesliveconfigexample.bean.ReloadBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BeanConfig {

    @Bean
    @ConfigurationProperties(prefix = "reload-bean")
    public ReloadBeanProperties reloadBeanProperties() {
        return new ReloadBeanProperties();
    }

    @Bean
    @RefreshScope
    public ReloadBean reloadBean(final ReloadBeanProperties reloadBeanProperties) {
        log.info("reloadBeanProperties: {}", reloadBeanProperties);
        return new ReloadBean(reloadBeanProperties);
    }
}
