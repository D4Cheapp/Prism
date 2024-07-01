package com.prism.authentication.auth.impl;

import com.prism.authentication.entity.Auth;
import com.prism.authentication.exception.UserAlreadyExistException;
import com.prism.authentication.exception.UserNotFoundException;
import com.prism.authentication.exception.password.IncorrectConfirmPasswordException;
import com.prism.authentication.model.EmailModel;
import com.prism.authentication.model.UserRegistrationModel;
import com.prism.authentication.repository.AuthRepo;
import com.prism.authentication.service.impl.EmailSenderServiceImpl;
import com.prism.authentication.util.AuthUtils;
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
    private final MockedStatic<AuthUtils> authUtilsMock = Mockito.mockStatic(AuthUtils.class);
    @Mock
    private AuthRepo authRepo;
    @InjectMocks
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
                () -> emailSenderService.sendEditUserEmailCode(currentUser, null, null));
        Mockito.when(authRepo.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(storedUser));
        Assertions.assertThrows(UserAlreadyExistException.class,
                () -> emailSenderService.sendEditUserEmailCode(currentUser, null, null));
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
                () -> emailSenderService.sendRegitrationCode(user, null));
        user.setConfirmPassword("123456123");
        Mockito.when(authRepo.findByEmail(ArgumentMatchers.anyString())).thenReturn(null);
        Assertions.assertThrows(IncorrectConfirmPasswordException.class,
                () -> emailSenderService.sendRegitrationCode(user, null));
    }

    @Test
    void testSendRestorePasswordCode() {
        Mockito.when(authRepo.findByEmail(ArgumentMatchers.anyString())).thenReturn(null);
        Assertions.assertThrows(UserNotFoundException.class,
                () -> emailSenderService.sendRestorePasswordCode("test1@gmail.com", null));
    }
}
