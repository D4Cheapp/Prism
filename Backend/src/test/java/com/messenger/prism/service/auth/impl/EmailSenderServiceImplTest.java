package com.messenger.prism.service.auth.impl;

import com.messenger.prism.entity.Auth;
import com.messenger.prism.exception.auth.UserAlreadyExistException;
import com.messenger.prism.exception.auth.UserNotFoundException;
import com.messenger.prism.exception.auth.password.IncorrectConfirmPasswordException;
import com.messenger.prism.model.auth.EmailModel;
import com.messenger.prism.model.auth.UserRegistrationModel;
import com.messenger.prism.repository.AuthRepo;
import com.messenger.prism.util.AuthUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceImplTest {
    @Mock
    private AuthRepo authRepo;
    @InjectMocks
    private EmailSenderServiceImpl emailSenderService;
    private final MockedStatic<AuthUtils> authUtilsMock = Mockito.mockStatic(AuthUtils.class);

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void teardown() {
        this.authUtilsMock.close();
    }

    @Test
    void testSendEditUserEmailCode() {
        Auth storedUser = new Auth();
        EmailModel currentUser = new EmailModel();
        currentUser.setId(1);
        currentUser.setEmail("test@gmail.com");
        Mockito.when(authRepo.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
        Mockito.when(authRepo.findByEmail(ArgumentMatchers.anyString())).thenReturn(storedUser);
        this.authUtilsMock.when(() -> AuthUtils.checkPermission(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any())).thenAnswer(i -> null);
        Assertions.assertThrows(UserNotFoundException.class,
                () -> emailSenderService.sendEditUserEmailCode(null, currentUser));
        Mockito.when(authRepo.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(storedUser));
        Assertions.assertThrows(UserAlreadyExistException.class,
                () -> emailSenderService.sendEditUserEmailCode(null, currentUser));
    }

    @Test
    void testSendRegistrationCode() {
        UserRegistrationModel user = new UserRegistrationModel();
        user.setEmail("test1@gmail.com");
        user.setPassword("123456");
        user.setConfirmPassword("123456");
        Mockito.when(authRepo.findByEmail(ArgumentMatchers.anyString())).thenReturn(new Auth());
        this.authUtilsMock.when(() -> AuthUtils.checkEmailValidity(ArgumentMatchers.anyString())).thenAnswer(i -> null);
        Assertions.assertThrows(UserAlreadyExistException.class,
                () -> emailSenderService.sendRegitrationCode(user));
        user.setConfirmPassword("123456123");
        Mockito.when(authRepo.findByEmail(ArgumentMatchers.anyString())).thenReturn(null);
        Assertions.assertThrows(IncorrectConfirmPasswordException.class,
                () -> emailSenderService.sendRegitrationCode(user));
    }

    @Test
    void testSendRestorePasswordCode() {
        Mockito.when(authRepo.findByEmail(ArgumentMatchers.anyString())).thenReturn(null);
        Assertions.assertThrows(UserNotFoundException.class,
                () -> emailSenderService.sendRestorePasswordCode("test1@gmail.com"));
    }
}
