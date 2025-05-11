package org.example.otpservice.model;

import java.time.Instant;
import java.util.Objects;

public class OtpCode {
    private int id;
    private int userId;
    private String operationId;
    private String code;
    private String status; // ACTIVE, EXPIRED, USED
    private Instant createdAt;
    private Instant expiresAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OtpCode otpCode)) return false;
        return id == otpCode.id && userId == otpCode.userId && Objects.equals(operationId, otpCode.operationId) && Objects.equals(code, otpCode.code) && Objects.equals(status, otpCode.status) && Objects.equals(createdAt, otpCode.createdAt) && Objects.equals(expiresAt, otpCode.expiresAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, operationId, code, status, createdAt, expiresAt);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}