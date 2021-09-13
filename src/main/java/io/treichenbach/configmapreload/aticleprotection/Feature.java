package io.treichenbach.configmapreload.aticleprotection;

import lombok.Data;

import java.util.List;

@Data
public class Feature {

    private List<Activation> activations;
    private String feature;

}
