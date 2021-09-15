package io.treichenbach.configmapreload.aticleprotection;

import lombok.Data;

import java.util.List;

@Data
public class Activation {

    private List<String> data;
    private String end;
    private String start;
}
