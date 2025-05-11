package org.example.otpservice.model;

import java.time.Instant;

public class OtpConfig {
    private int codeLength;
    private int ttlSeconds;

    public int getCodeLength() { return codeLength; }
    public void setCodeLength(int codeLength) { this.codeLength = codeLength; }
    public int getTtlSeconds() { return ttlSeconds; }
    public void setTtlSeconds(int ttlSeconds) { this.ttlSeconds = ttlSeconds; }
}


