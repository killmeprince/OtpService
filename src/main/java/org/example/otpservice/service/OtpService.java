package org.example.otpservice.service;

import org.example.otpservice.dao.OtpConfigDao;
import org.example.otpservice.dao.OtpCodeDao;
import org.example.otpservice.model.OtpCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;

@Service
public class OtpService {
//    private final UserDao userDao;
    private final OtpConfigDao configDao;
    private final OtpCodeDao codeDao;
    private final SecureRandom random = new SecureRandom();
    public OtpService(OtpConfigDao configDao, OtpCodeDao codeDao) {
//        this.userDao = userDao;
        this.configDao = configDao;
        this.codeDao = codeDao;
    }
    public String generateCode(int userId, String operationId) {
        var cfg = configDao.getConfig();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cfg.getCodeLength(); i++) {
            sb.append(random.nextInt(10));
        }
        String code = sb.toString();

        OtpCode o = new OtpCode();
        o.setUserId(userId);
        o.setOperationId(operationId);
        o.setCode(code);
        o.setStatus("ACTIVE");
        o.setCreatedAt(Instant.now());
        o.setExpiresAt(Instant.now().plusSeconds(cfg.getTtlSeconds()));

        codeDao.save(o);
        return code;
    }
    public boolean validateCode(int userId, String operationId, String code) {
        codeDao.expireAll();
        Optional<OtpCode> opt = codeDao.findActive(userId, operationId, code);
        if (opt.isEmpty()) {
            return false;
        }
        codeDao.markUsed(opt.get().getId());
        return true;
    }
    @Scheduled(cron = "${scheduler.expire-otp.cron}")
    public void expireScheduler() {
        codeDao.expireAll();
    }
}
