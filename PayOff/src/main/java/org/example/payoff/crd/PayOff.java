package org.example.payoff.crd;

import io.fabric8.kubernetes.api.model.Doneable;
import io.sundr.builder.annotations.Buildable;
import io.sundr.builder.annotations.BuildableReference;
import lombok.Data;

@Data
@Buildable(editableEnabled = false, generateBuilderPackage = false, builderPackage = "io.fabric8.kubernetes.api.builder", refs = {
        @BuildableReference(io.fabric8.kubernetes.api.model.ObjectMeta.class)
})
public class PayOff {
    private String apiVersion;
    private String kind;
    private Metadata metadata;
    private Spec spec;

    @Data
    public static class Metadata {
        private String name;
        private String namespace;
    }

    @Data
    public static class Spec {
        private String type; // deploy 或 delete
        private String yamlConfig; // Deployment YAML 配置
    }
}