package knu.gpu.gpusharing.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class SessionRepository {

    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;

    private String keySession(String sessionId) {
        return "session:" + sessionId;
    }

    private String keyUserSessions(String userId) {
        return "user:" + userId + ":sessions";
    }

    public void save(GpuSession session) {
        String key = keySession(session.getId());
        String json;
        try {
            json = objectMapper.writeValueAsString(session);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize session " + session.getId(), e);
        }

        // TTL 계산
        Duration ttl = Duration.between(Instant.now(), session.getExpiresAt());
        long ttlSeconds = Math.max(ttl.getSeconds(), 0);

        redis.opsForValue().set(key, json, ttlSeconds, TimeUnit.SECONDS);
        redis.opsForList().rightPush(keyUserSessions(session.getUserId()), session.getId());
    }

    public GpuSession findById(String sessionId) {
        String key = keySession(sessionId);
        String json = redis.opsForValue().get(key);
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, GpuSession.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize session " + sessionId, e);
        }
    }

    public void delete(String sessionId, String userId) {
        String key = keySession(sessionId);
        redis.delete(key);
        if (userId != null) {
            redis.opsForList().remove(keyUserSessions(userId), 1, sessionId);
        }
    }
}
