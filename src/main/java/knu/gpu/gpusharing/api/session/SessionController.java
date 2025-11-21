package knu.gpu.gpusharing.api.session;

import knu.gpu.gpusharing.api.session.dto.SessionCreateRequest;
import knu.gpu.gpusharing.api.session.dto.SessionResponse;
import knu.gpu.gpusharing.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<SessionResponse> createSession(
            @RequestBody SessionCreateRequest request
    ) {
        SessionResponse response = sessionService.createSession(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionResponse> getSession(
            @PathVariable String sessionId
    ) {
        SessionResponse response = sessionService.getSession(sessionId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> terminateSession(
            @PathVariable String sessionId
    ) {
        sessionService.terminateSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}
