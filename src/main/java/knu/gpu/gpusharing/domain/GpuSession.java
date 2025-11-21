package knu.gpu.gpusharing.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

@Getter
public class GpuSession {
    private String id;

    private String userId;
    private String gpuType;
    private int gpuCount;
    private SessionStatus status;
    private int durationMinutes;

    private Instant createdAt;
    private Instant expiresAt;

    private String namespace;
    private String podName;

    public long getRemainingSeconds() {
        return Duration.between(Instant.now(), this.expiresAt).getSeconds();
    }

    public void activate(String namespace, String podName) {
        this.status = SessionStatus.ACTIVE;
        this.namespace = namespace;
        this.podName = podName;
    }

    public GpuSession(String id, String userId, String gpuType, int gpuCount, int durationMinutes,
                      Instant createdAt, Instant expiresAt, SessionStatus status) {
        this.id = id;
        this.userId = userId;
        this.gpuType = gpuType;
        this.gpuCount = gpuCount;
        this.durationMinutes = durationMinutes;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.status = status;
    }
}
