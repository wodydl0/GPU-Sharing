package knu.gpu.gpusharing.domain;

public enum SessionStatus {
    WAITING,     // 리소스 할당 대기 중 (큐에 들어가 있음)
    ACTIVE,      // GPU 자원 확보 & 사용 중
    TERMINATED,  // 사용자가 종료
    EXPIRED,     // TTL 만료로 자연 종료
    FAILED       // 리소스 할당 실패 등
}
