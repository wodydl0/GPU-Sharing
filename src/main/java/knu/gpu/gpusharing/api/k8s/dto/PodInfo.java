package knu.gpu.gpusharing.api.k8s.dto;

import io.fabric8.kubernetes.api.model.Pod;

public record PodInfo(
        String name,
        String phase,
        String podIP,
        String nodeName
) {
    public static PodInfo from(Pod pod) {
        String name = pod.getMetadata().getName();
        String phase = pod.getStatus().getPhase();
        String podIP = pod.getStatus().getPodIP();
        String nodeName = pod.getSpec().getNodeName();

        return new PodInfo(name, phase, podIP, nodeName);
    }
}
