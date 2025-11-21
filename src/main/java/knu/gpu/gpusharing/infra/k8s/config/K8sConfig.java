package knu.gpu.gpusharing.infra.k8s.config;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@EnableConfigurationProperties(K8sProperties.class)
@RequiredArgsConstructor
public class K8sConfig {
    private final K8sProperties k8sProperties;

    @Bean
    public KubernetesClient kubernetesClient() throws IOException {
        String raw = Files.readString(Path.of(k8sProperties.kubeconfigPath()));
        Config config = Config.fromKubeconfig(null, raw, k8sProperties.kubeconfigPath());
        return new KubernetesClientBuilder().withConfig(config).build();
    }
}
