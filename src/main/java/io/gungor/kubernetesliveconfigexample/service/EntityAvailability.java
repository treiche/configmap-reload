package io.gungor.kubernetesliveconfigexample.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gungor.kubernetesliveconfigexample.aticleprotection.ArticleProtection;
import io.gungor.kubernetesliveconfigexample.aticleprotection.Feature;
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
public class EntityAvailability {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String FEATURE = "DISABLE_RECO";

    private List<String> unavailableEntities;

    public EntityAvailability() {
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
                    new String(Files.readAllBytes(Paths.get("/opt/article-protection-config/protected-articles.json")));

            final ArticleProtection articleProtection = MAPPER.readValue(protectedArticles, ArticleProtection.class);
            final Optional<Feature> feature = articleProtection.getFeatures().stream()
                                                               .filter(f -> FEATURE.equals(f.getFeature()))
                                                               .collect(Collectors.toList()).stream().findFirst();

            feature.ifPresent(f -> f.getActivations().forEach(activation -> {
                final List<String> entities = activation.getConfigSkus().stream()
                                                        .map(sku -> "ern:product::" + sku)
                                                        .collect(Collectors.toList());
                allEntities.addAll(entities);
            }));

        } catch (IOException e) {
            log.warn("unable to get protected articles from ConfigMap", e);
        }

        return allEntities.stream().distinct().sorted().collect(Collectors.toList());
    }
}
