package com.prism.messenger.service.auth.impl;

import com.prism.messenger.entity.Auth;
import com.prism.messenger.exception.ActivationCodeExpireException;
import com.prism.messenger.exception.UserNotFoundException;
import com.prism.messenger.exception.password.IncorrectConfirmPasswordException;
import com.prism.messenger.exception.password.IncorrectPasswordException;
import com.prism.messenger.model.ActivationCodeModel;
import com.prism.messenger.model.EditPasswordModel;
import com.prism.messenger.model.RestorePasswordModel;
import com.prism.messenger.model.UserLoginModel;
import com.prism.messenger.repository.AuthRepo;
import com.prism.messenger.service.impl.AuthServiceImpl;
import com.prism.messenger.service.impl.EmailSenderServiceImpl;
import com.prism.messenger.util.AuthUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    private final MockedStatic<AuthUtils> authUtilsMock = Mockito.mockStatic(AuthUtils.class);
    @Mock
    private AuthRepo authRepo;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private EmailSenderServiceImpl emailSenderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void teardown() {
        this.authUtilsMock.close();
    }

    @Test
    void testLogin() {
        Auth storedUser = new Auth();
        storedUser.setEmail("test@test.com");
        storedUser.setPassword(encoder.encode("test"));
        UserLoginModel user = UserLoginModel.toModel(storedUser);
        user.setPassword("test");
        Mockito.when(authRepo.findByEmail(ArgumentMatchers.anyString())).thenReturn(null);
        Assertions.assertThrows(UserNotFoundException.class, () -> authService.login(user));
        Mockito.when(authRepo.findByEmail(ArgumentMatchers.anyString())).thenReturn(storedUser);
        Assertions.assertThrows(IncorrectPasswordException.class, () -> authService.login(user));
    }

    @Test
    void testDeleteUser() {
        Auth storedUser = new Auth();
        storedUser.setId(1);
        Mockito.when(authRepo.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
        this.authUtilsMock.when(() -> AuthUtils.checkPermission(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenAnswer(i -> null);
        Assertions.assertThrows(UserNotFoundException.class, () -> authService.deleteUser(null,
                null, 1));
    }

    @Test
    void testEditUserPassword() {
        EditPasswordModel passwords = new EditPasswordModel();
        passwords.setId(1);
        passwords.setOldPassword("test1");
        Auth storedUser = new Auth();
        storedUser.setPassword(encoder.encode("test"));
        this.authUtilsMock.when(() -> AuthUtils.checkPermission(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenAnswer(i -> null);
        Mockito.when(authRepo.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class,
                () -> authService.editUserPassword(null, passwords));
        Mockito.when(authRepo.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(storedUser));
        Assertions.assertThrows(IncorrectPasswordException.class,
                () -> authService.editUserPassword(null, passwords));
    }

    @Test
    void testRestoreUserPassword() throws ActivationCodeExpireException {
        ActivationCodeModel user = new ActivationCodeModel();
        user.setCode("123");
        RestorePasswordModel passwords = new RestorePasswordModel();
        passwords.setPassword("test");
        passwords.setConfirmPassword("test2");
        passwords.setCode("123");
        Mockito.when(emailSenderService.getUserByEmail(ArgumentMatchers.anyString())).thenReturn(user);
        this.authUtilsMock.when(() -> AuthUtils.checkPasswordValidity(ArgumentMatchers.anyString())).thenAnswer(i -> null);
        Assertions.assertThrows(IncorrectConfirmPasswordException.class,
                () -> authService.restoreUserPassword("test@test.com", passwords));
    }
}
