package knu.gpu.gpusharing.infra.k8s.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "k8s")
public record K8sProperties(
        String kubeconfigPath,
        String namespace
) {
}
