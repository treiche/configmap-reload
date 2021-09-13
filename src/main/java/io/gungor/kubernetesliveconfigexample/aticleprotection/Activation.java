package io.gungor.kubernetesliveconfigexample.aticleprotection;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Activation {

    @JsonProperty("config_skus")
    private List<String> configSkus;
    private String end;
    private String start;
}
