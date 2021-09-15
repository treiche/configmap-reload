package io.treichenbach.configmapreload.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.treichenbach.configmapreload.aticleprotection.ArticleProtection;
import io.treichenbach.configmapreload.aticleprotection.Feature;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@Data
public class EntityAvailabilityUpdater {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String FEATURE = "NEW_FEATURE";
    private static final String MOUNTED_FILE_PATH = "/opt/article-protection-config/protected-articles.json";

    private List<String> unavailableEntities;

    public EntityAvailabilityUpdater() {
        unavailableEntities = getEntitiesFromConfigMap();
    }

    @Scheduled(fixedDelay = 30 * 1000)
    private void refresh() {

        log.info("refresh unavailableEntities");

        final List<String> newUnavailableSkus = getEntitiesFromConfigMap();
        if (!unavailableEntities.equals(newUnavailableSkus)) {

            log.info("changes detected, update unavailableEntities");

            final List<String> added = new ArrayList<>(newUnavailableSkus);
            added.removeAll(unavailableEntities);

            final List<String> removed = new ArrayList<>(unavailableEntities);
            removed.removeAll(newUnavailableSkus);
            log.info("Changing unavailable skus: added:{}, removed:{}, new list:{}",
                     added.isEmpty() ? "-" : String.join(", ", added),
                     removed.isEmpty() ? "-" : String.join(", ", removed),
                     String.join(", ", newUnavailableSkus));
            unavailableEntities = newUnavailableSkus;
        }
    }

    private List<String> getEntitiesFromConfigMap() {

        final List<String> allEntities = new ArrayList<>();

        try {
            final String protectedArticles =
                    new String(Files.readAllBytes(Paths.get(MOUNTED_FILE_PATH)));

            final ArticleProtection articleProtection = MAPPER.readValue(protectedArticles, ArticleProtection.class);
            final Optional<Feature> feature = articleProtection.getFeatures().stream()
                                                               .filter(f -> FEATURE.equals(f.getFeature()))
                                                               .collect(Collectors.toList()).stream().findFirst();

            feature.ifPresent(f -> f.getActivations().forEach(activation -> allEntities.addAll(activation.getData())));

        } catch (IOException e) {
            log.warn("unable to get protected articles from ConfigMap", e);
        }

        return allEntities.stream().distinct().sorted().collect(Collectors.toList());
    }
}
