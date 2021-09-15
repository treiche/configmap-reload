package io.treichenbach.configmapreload.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@Data
public class ConfigMapUpdater {

    private static final String LOCAL_PATH = "config/";
    private static final String MOUNTED_FILE_PATH = "/opt/mounted-config-map/application-override.yaml";
    private static final String FILE_NAME = "application-override.yaml";

    private final ContextRefresher contextRefresher;
    private final RefreshScope refreshScope;
    private final ConfigurableApplicationContext context;

    public ConfigMapUpdater(final ContextRefresher contextRefresher, final RefreshScope refreshScope, final ConfigurableApplicationContext context) {
        this.contextRefresher = contextRefresher;
        this.refreshScope = refreshScope;
        this.context = context;
    }

    @Scheduled(fixedDelay = 20 * 1000)
    private void refresh() {
        log.info("refresh config from mounted ConfigMap");
        try {
            // get content from config-map
            final String overrideConfig = new String(Files.readAllBytes(Paths.get(MOUNTED_FILE_PATH)));
            overrideConfig.lines().forEach(log::info);

            // create application-override.yaml
            if (!Files.exists(Paths.get(LOCAL_PATH))) {
                Files.createDirectories(Paths.get(LOCAL_PATH));
            }
            final Path path = Paths.get(LOCAL_PATH, FILE_NAME);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            final Path localPath = Files.writeString(path, overrideConfig, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            log.info("override config saved: {}", localPath.toString());

            // refresh spring context
            final Set<String> keys = contextRefresher.refreshEnvironment();
            log.info("keys: {} updated", keys);

            if (!keys.isEmpty()) {
                // re-initiate refreshable beans - will get their new configuration
                // could be replaced with refreshScope.refreshAll()
                final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
                final Map<String, Object> beans =
                        beanFactory.getBeansWithAnnotation(org.springframework.cloud.context.config.annotation.RefreshScope.class);
                beans.keySet().forEach(name -> {
                    log.info("[{}] invoking destruction", name);
                    refreshScope.refresh(name);
                });
            }

        } catch (IOException e) {
            log.warn("unable to get configuration from mounted ConfigMap", e);
        }
    }
}
