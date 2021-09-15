package io.treichenbach.configmapreload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "config")
public class SpringConfigProperties {

    private String value;
    private String beanvalue;

}
