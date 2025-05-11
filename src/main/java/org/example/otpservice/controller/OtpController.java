package org.example.otpservice.controller;

import org.example.otpservice.dao.UserDao;
import org.example.otpservice.service.NotificationService;
import org.example.otpservice.service.OtpService;
import org.example.otpservice.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;
    private final UserDao userDao;
    private final NotificationService notificationService;

    public OtpController(OtpService otpService, UserDao userDao, NotificationService notificationService) {
        this.otpService = otpService;
        this.userDao = userDao;
        this.notificationService = notificationService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generate(@RequestParam String operationId,
                                      @RequestParam String via,
                                      @RequestParam String target,
                                      Principal principal) {
        int userId = userDao.findByUsername(principal.getName()).get().getId();
        String code = otpService.generateCode(userId, operationId);
        notificationService.notify(via, target, code);
        return ResponseEntity.ok(Map.of("sentVia", via));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(
            @RequestParam String operationId,
            @RequestParam String code,
            Principal principal
    ) {
        User user = userDao.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        int userId = user.getId();

        boolean ok = otpService.validateCode(userId, operationId, code);
        if (ok) {
            return ResponseEntity.ok(Map.of("valid", true));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("valid", false, "error", "Invalid or expired code"));
        }
    }
}
