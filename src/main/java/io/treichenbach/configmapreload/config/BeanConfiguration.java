package io.treichenbach.configmapreload.config;

import io.treichenbach.configmapreload.bean.ReloadBeanConfig;
import io.treichenbach.configmapreload.bean.ReloadBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class BeanConfiguration {

    private final SpringConfigProperties springConfigProperties;

    public BeanConfiguration(final SpringConfigProperties springConfigProperties) {
        this.springConfigProperties = springConfigProperties;
    }

    @Bean
    @RefreshScope
    public ReloadBean reloadBean() {
        log.info("springConfigProperties: {}", springConfigProperties);

        final ReloadBeanConfig reloadBeanConfig = new ReloadBeanConfig();
        reloadBeanConfig.setValue(springConfigProperties.getBeanvalue());

        return ReloadBean.create(reloadBeanConfig);
    }
}
