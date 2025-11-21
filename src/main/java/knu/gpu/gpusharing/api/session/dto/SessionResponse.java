package knu.gpu.gpusharing.api.session.dto;

import knu.gpu.gpusharing.domain.GpuSession;
import knu.gpu.gpusharing.domain.SessionStatus;

import java.time.Instant;

public record SessionResponse(
        String id,
        String userId,
        String gpuType,
        int gpuCount,
        Instant createdAt,
        Instant expiresAt,
        SessionStatus status,
        long remainingSeconds
) {
    public static SessionResponse of(GpuSession session) {
        return new SessionResponse(
                session.getId(),
                session.getUserId(),
                session.getGpuType(),
                session.getGpuCount(),
                session.getCreatedAt(),
                session.getExpiresAt(),
                session.getStatus(),
                session.getRemainingSeconds()
        );

    }
}
