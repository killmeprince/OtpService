package org.example.otpservice.service;

import org.example.otpservice.dao.UserDao;
import org.example.otpservice.dto.LoginRequest;
import org.example.otpservice.dto.LoginResponse;
import org.example.otpservice.dto.RegistrationRequest;
import org.example.otpservice.model.User;
import org.example.otpservice.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserDao userDao, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest req) {
        User user = userDao.findByUsername(req.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new LoginResponse(token);
    }
    public void register(RegistrationRequest req) {

        if (userDao.findByUsername(req.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        if ("ADMIN".equalsIgnoreCase(req.getRole()) && userDao.countAdmins() > 0) {
            throw new IllegalArgumentException("Admin already exists");
        }

        String hash = passwordEncoder.encode(req.getPassword());
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPasswordHash(hash);
        u.setRole(req.getRole().toUpperCase());
        userDao.save(u);
    }
}
