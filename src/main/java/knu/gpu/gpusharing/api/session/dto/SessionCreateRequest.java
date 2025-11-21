package knu.gpu.gpusharing.api.session.dto;

import knu.gpu.gpusharing.domain.GpuSession;
import knu.gpu.gpusharing.domain.SessionStatus;

import java.time.Instant;

public record SessionCreateRequest(
        String userId,
        String gpuType,
        int gpuCount,
        int durationMinutes
) {
    public static GpuSession toEntity(SessionCreateRequest request,
                                      String gpuSessionId,
                                      Instant createdAt,
                                      Instant expiresAt,
                                      SessionStatus status) {
        return new GpuSession(
                gpuSessionId,
                request.userId(),
                request.gpuType(),
                request.gpuCount(),
                request.durationMinutes(),
                createdAt,
                expiresAt,
                status
        );
    }
}
