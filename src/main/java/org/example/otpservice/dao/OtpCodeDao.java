package org.example.otpservice.dao;

import org.example.otpservice.model.OtpCode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.Optional;
@Repository
public class OtpCodeDao {
    private final JdbcTemplate jdbc;
    private final RowMapper<OtpCode> codeMapper = (ResultSet rs, int rn) -> {
        OtpCode o = new OtpCode();
        o.setId(rs.getInt("id"));
        o.setUserId(rs.getInt("user_id"));
        o.setOperationId(rs.getString("operation_id"));
        o.setCode(rs.getString("code"));
        o.setStatus(rs.getString("status"));
        o.setCreatedAt(rs.getTimestamp("created_at").toInstant());
        o.setExpiresAt(rs.getTimestamp("expires_at").toInstant());
        return o;
    };
    public OtpCodeDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public void save(OtpCode o) {
        jdbc.update("""
            INSERT INTO otp_codes (user_id, operation_id, code, status, expires_at)
             VALUES (?,?,?,?,?)
            """,
                o.getUserId(), o.getOperationId(), o.getCode(),
                o.getStatus(), java.sql.Timestamp.from(o.getExpiresAt())
        );
    }

    public Optional<OtpCode> findActive(int userId, String operationId, String code) {
        String sql = """
            SELECT * FROM otp_codes
             WHERE user_id=? AND operation_id=? AND code=?
               AND status='ACTIVE'
             """;
        return jdbc.query(sql, codeMapper, userId, operationId, code)
                .stream().findFirst();
    }

    public void markUsed(int id) {
        jdbc.update("UPDATE otp_codes SET status='USED' WHERE id=?", id);
    }
    public void expireAll() {
        jdbc.update("""
            UPDATE otp_codes SET status='EXPIRED'
             WHERE status='ACTIVE' AND expires_at < now()
            """);
    }
}