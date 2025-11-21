package knu.gpu.gpusharing.infra.k8s;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import knu.gpu.gpusharing.api.k8s.dto.PodInfo;
import knu.gpu.gpusharing.infra.k8s.config.K8sProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class K8sSessionReader {

    private final KubernetesClient client;
    private final K8sProperties k8sProperties;

    public List<PodInfo> listAllSessionPods() {
        String ns = k8sProperties.namespace();
        return client.pods()
                .inNamespace(ns)
                .list()
                .getItems()
                .stream()
                .map(PodInfo::from)
                .toList();
    }

    public PodInfo getPodOfSession(String sessionId) {
        String ns = k8sProperties.namespace();
        String podName = "session-" + sessionId;

        Pod pod = client.pods()
                .inNamespace(ns)
                .withName(podName)
                .get();

        if (pod == null) {
            throw new IllegalArgumentException("Pod not found: " + podName);
        }

        return PodInfo.from(pod);
    }


}
