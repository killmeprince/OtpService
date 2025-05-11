package org.example.otpservice.dao;

import org.example.otpservice.model.OtpConfig;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OtpConfigDao {
    private final JdbcTemplate jdbc;
    private final RowMapper<OtpConfig> cfgMapper = (rs, rn) -> {
        OtpConfig c = new OtpConfig();
        c.setCodeLength(rs.getInt("code_length"));
        c.setTtlSeconds(rs.getInt("ttl_seconds"));
        return c;
    };
    public OtpConfigDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public OtpConfig getConfig() {
        return jdbc.queryForObject("SELECT * FROM otp_config", cfgMapper);
    }
    public void updateConfig(int codeLength, int ttlSeconds) {
        jdbc.update("UPDATE otp_config SET code_length=?, ttl_seconds=?",
                codeLength, ttlSeconds);
    }
}
