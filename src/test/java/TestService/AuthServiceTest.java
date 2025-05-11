package TestService;

import org.example.otpservice.dao.UserDao;
import org.example.otpservice.dto.RegistrationRequest;
import org.example.otpservice.model.User;
import org.example.otpservice.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        // MockitoExtension автоматически инжектит моки в authService
    }

    @Test
    void register_ShouldSaveUser_WhenValidUser() {
        // given
        RegistrationRequest req = new RegistrationRequest();
        req.setUsername("alice");
        req.setPassword("secret");
        req.setRole("user");

        when(userDao.findByUsername("alice")).thenReturn(Optional.empty());
        // убрали when(userDao.countAdmins()) — он не нужен для USER
        when(passwordEncoder.encode("secret")).thenReturn("hashedSecret");

        // when
        authService.register(req);

        // then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userDao).save(captor.capture());
        User saved = captor.getValue();

        assertEquals("alice", saved.getUsername());
        assertEquals("hashedSecret", saved.getPasswordHash());
        assertEquals("USER", saved.getRole());
    }

    @Test
    void register_ShouldThrow_WhenUsernameExists() {
        // given
        RegistrationRequest req = new RegistrationRequest();
        req.setUsername("bob");
        req.setPassword("pass");
        req.setRole("USER");

        when(userDao.findByUsername("bob"))
                .thenReturn(Optional.of(new User()));

        // when / then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(req)
        );
        assertEquals("Username already exists", ex.getMessage());
        verify(userDao, never()).save(any());
    }

    @Test
    void register_ShouldThrow_WhenSecondAdmin() {
        // given
        RegistrationRequest req = new RegistrationRequest();
        req.setUsername("admin2");
        req.setPassword("pw");
        req.setRole("ADMIN");

        when(userDao.findByUsername("admin2")).thenReturn(Optional.empty());
        when(userDao.countAdmins()).thenReturn(1);

        // when / then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(req)
        );
        assertEquals("Admin already exists", ex.getMessage());
        verify(userDao, never()).save(any());
    }

    @Test
    void register_ShouldAllowFirstAdmin() {
        // given
        RegistrationRequest req = new RegistrationRequest();
        req.setUsername("theAdmin");
        req.setPassword("pw123");
        req.setRole("ADMIN");

        when(userDao.findByUsername("theAdmin")).thenReturn(Optional.empty());
        when(userDao.countAdmins()).thenReturn(0);
        when(passwordEncoder.encode("pw123")).thenReturn("hPw123");

        // when
        authService.register(req);

        // then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userDao).save(captor.capture());
        User saved = captor.getValue();
        assertEquals("ADMIN", saved.getRole());
        assertEquals("hPw123", saved.getPasswordHash());
    }
}
