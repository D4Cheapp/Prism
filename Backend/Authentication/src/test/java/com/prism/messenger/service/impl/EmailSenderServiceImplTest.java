package com.prism.messenger.service.impl;

import com.prism.messenger.entity.Auth;
import com.prism.messenger.exception.UserAlreadyExistException;
import com.prism.messenger.exception.UserNotFoundException;
import com.prism.messenger.exception.password.IncorrectConfirmPasswordException;
import com.prism.messenger.model.ChangeEmailModel;
import com.prism.messenger.model.UserRegistrationModel;
import com.prism.messenger.repository.AuthRepo;
import com.prism.messenger.util.AuthUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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
  void testSendEditUserEmailCode() throws UserNotFoundException {
    Auth storedUser = new Auth();
    ChangeEmailModel currentUser = new ChangeEmailModel();
    currentUser.setId(1);
    currentUser.setEmail("test@gmail.com");
    Mockito.when(AuthUtils.getUser(ArgumentMatchers.anyInt(), ArgumentMatchers.any()))
        .thenReturn(null);
    Mockito.when(authRepo.findByEmail(ArgumentMatchers.anyString())).thenReturn(storedUser);
    this.authUtilsMock.when(
        () -> AuthUtils.checkPermission(ArgumentMatchers.any(), ArgumentMatchers.any(),
            ArgumentMatchers.any())).thenAnswer(i -> null);
    Mockito.when(AuthUtils.getUser(ArgumentMatchers.anyInt(), ArgumentMatchers.any()))
        .thenReturn(storedUser);
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
    this.authUtilsMock.when(() -> AuthUtils.checkEmailValidity(ArgumentMatchers.anyString()))
        .thenAnswer(i -> null);
    Assertions.assertThrows(UserAlreadyExistException.class,
        () -> emailSenderService.sendRegistrationCode(user, null));
    user.setConfirmPassword("123456123");
    Mockito.when(authRepo.findByEmail(ArgumentMatchers.anyString())).thenReturn(null);
    Assertions.assertThrows(IncorrectConfirmPasswordException.class,
        () -> emailSenderService.sendRegistrationCode(user, null));
  }

  @Test
  void testSendRestorePasswordCode() {
    Mockito.when(authRepo.findByEmail(ArgumentMatchers.anyString())).thenReturn(null);
    Assertions.assertThrows(UserNotFoundException.class,
        () -> emailSenderService.sendRestorePasswordCode("test1@gmail.com", null));
  }
}
