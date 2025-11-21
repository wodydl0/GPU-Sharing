package knu.gpu.gpusharing.api.k8s;

import knu.gpu.gpusharing.api.k8s.dto.PodInfo;
import knu.gpu.gpusharing.infra.k8s.K8sSessionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/k8s")
@RequiredArgsConstructor
public class K8sController {

    private final K8sSessionReader k8sSessionReader;

    @GetMapping("/pods")
    public ResponseEntity<List<PodInfo>> listPods() {

        return ResponseEntity.ok(k8sSessionReader.listAllSessionPods());
    }

    @GetMapping("/pods/{sessionId}")
    public ResponseEntity<PodInfo> getPod(@PathVariable String sessionId) {

        return ResponseEntity.ok(k8sSessionReader.getPodOfSession(sessionId));
    }
}
