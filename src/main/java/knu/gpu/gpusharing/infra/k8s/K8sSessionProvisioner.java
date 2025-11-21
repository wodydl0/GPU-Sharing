package knu.gpu.gpusharing.infra.k8s;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import knu.gpu.gpusharing.domain.GpuSession;
import knu.gpu.gpusharing.infra.k8s.config.K8sProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class K8sSessionProvisioner {

    private final KubernetesClient client;
    private final K8sProperties k8sProperties;

    public String provision(GpuSession session) {
        String ns = k8sProperties.namespace();
        String podName = "session-" + session.getId();

        Pod pod = new PodBuilder()
                .withNewMetadata()
                .withName(podName)
                .withNamespace(ns)
                .addToLabels("app", "gpu-session")
                .addToLabels("sessionId", session.getId())
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName("main")
                .withImage("python:3.10")
                .withCommand("sleep")
                .withArgs("infinity")
                .addNewEnv()
                .withName("SESSION_ID")
                .withValue(session.getId())
                .endEnv()
                .endContainer()
                .endSpec()
                .build();

        client.pods()
                .inNamespace(ns)
                .resource(pod)
                .create();

        log.info("Created pod {} for session {}", podName, session.getId());
        return podName;
    }

    public String getNamespace() {
        return k8sProperties.namespace();
    }
}
