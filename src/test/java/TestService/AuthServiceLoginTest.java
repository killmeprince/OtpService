package TestService;

import org.example.otpservice.dao.UserDao;
import org.example.otpservice.dto.LoginRequest;
import org.example.otpservice.dto.LoginResponse;
import org.example.otpservice.model.User;
import org.example.otpservice.security.JwtUtil;
import org.example.otpservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceLoginTest {

    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_ShouldReturnToken_WhenCredentialsValid() {
        String username = "john";
        String rawPwd  = "secret";
        String pwdHash = "hashedSecret";
        String role    = "USER";
        String fakeToken = "jwt-token-123";

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(pwdHash);
        user.setRole(role);

        when(userDao.findByUsername(username))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPwd, pwdHash))
                .thenReturn(true);
        when(jwtUtil.generateToken(username, role))
                .thenReturn(fakeToken);

        LoginResponse resp = authService.login(new LoginRequest() {{
            setUsername(username);
            setPassword(rawPwd);
        }});


        assertNotNull(resp);
        assertEquals(fakeToken, resp.getToken());
        verify(jwtUtil).generateToken(username, role);
    }

    @Test
    void login_ShouldThrow_WhenUserNotFound() {

        when(userDao.findByUsername("noone"))
                .thenReturn(Optional.empty());


        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.login(new LoginRequest() {{
                    setUsername("noone");
                    setPassword("whatever");
                }})
        );
        assertEquals("Invalid credentials", ex.getMessage());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtUtil, never()).generateToken(any(), any());
    }

    @Test
    void login_ShouldThrow_WhenPasswordMismatch() {
        String username = "joe";
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash("correctHash");
        user.setRole("USER");

        when(userDao.findByUsername(username))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPwd", "correctHash"))
                .thenReturn(false);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.login(new LoginRequest() {{
                    setUsername(username);
                    setPassword("wrongPwd");
                }})
        );
        assertEquals("Invalid credentials", ex.getMessage());
        verify(jwtUtil, never()).generateToken(any(), any());
    }
}
