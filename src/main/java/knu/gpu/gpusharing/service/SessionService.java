package knu.gpu.gpusharing.service;

import knu.gpu.gpusharing.api.session.dto.SessionCreateRequest;
import knu.gpu.gpusharing.api.session.dto.SessionResponse;
import knu.gpu.gpusharing.domain.GpuSession;
import knu.gpu.gpusharing.domain.SessionRepository;
import knu.gpu.gpusharing.domain.SessionStatus;
import knu.gpu.gpusharing.infra.k8s.K8sSessionProvisioner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final K8sSessionProvisioner provisioner;

    public SessionResponse createSession(SessionCreateRequest request) {
        String gpuSessionId = UUID.randomUUID().toString().substring(0, 8);
        Instant now = Instant.now();
        Instant expiresAt = now.plus(Duration.ofMinutes(request.durationMinutes()));

        GpuSession session = SessionCreateRequest
                .toEntity(request, gpuSessionId, now, expiresAt, SessionStatus.WAITING);

        sessionRepository.save(session);

        // Pod 생성 요청
        String podName = provisioner.provision(session);

        // namespace는 Properties에서 가져온 값
        session.activate(provisioner.getNamespace(), podName);

        sessionRepository.save(session);

        return SessionResponse.of(session);
    }

    public SessionResponse getSession(String sessionId) {
        GpuSession session = sessionRepository.findById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Session not found: " + sessionId);
        }

        return SessionResponse.of(session);

    }

    public void terminateSession(String sessionId) {
        GpuSession session = sessionRepository.findById(sessionId);
        if (session == null) {
            return;
        }
        sessionRepository.delete(sessionId, session.getUserId());
    }
}
