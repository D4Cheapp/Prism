package com.messenger.prism.util;

import com.messenger.prism.entity.Auth;
import com.messenger.prism.exception.PermissionsException;
import com.messenger.prism.exception.auth.email.EmptyEmailException;
import com.messenger.prism.exception.auth.email.IncorectEmailException;
import com.messenger.prism.exception.auth.password.EmptyPasswordException;
import com.messenger.prism.exception.auth.password.PasswordIsTooWeakException;
import com.messenger.prism.exception.auth.password.TooLongPasswordException;
import com.messenger.prism.exception.auth.password.TooShortPasswordException;
import com.messenger.prism.repository.AuthRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthUtilsTest {
    @Mock
    private AuthRepo storeUserRepo;
    @Mock
    private AuthenticationProvider authenticationProvider;

    @Test
    void testCheckPermissionsUser() {
        Authentication auth = new UsernamePasswordAuthenticationToken("email", "password");
        Auth storedUser = new Auth();
        Auth currentUser = new Auth();
        currentUser.setId(1);
        currentUser.setRole("USER");
        storedUser.setId(2);
        storedUser.setRole("USER");
        Mockito.when(storeUserRepo.findByEmail(ArgumentMatchers.anyString())).thenReturn(currentUser);
        Assertions.assertThrows(PermissionsException.class, () -> AuthUtils.checkPermission(auth,
                Optional.of(storedUser), storeUserRepo));
    }

    @Test
    void testPasswordValidity() {
        Assertions.assertAll("Check password validity",
                () -> Assertions.assertThrows(EmptyPasswordException.class,
                        () -> AuthUtils.checkPasswordValidity("")),
                () -> Assertions.assertThrows(TooShortPasswordException.class,
                        () -> AuthUtils.checkPasswordValidity("12345")),
                () -> Assertions.assertThrows(TooLongPasswordException.class,
                        () -> AuthUtils.checkPasswordValidity(
                                "1234567890123456789012345678901234567890123456789")));
        Assertions.assertDoesNotThrow(() -> AuthUtils.isPasswordTooWeak("*3r9cv9OkF{su"));
    }

    @Test
    void testPasswordWeakness() {
        final String weakPasswordError =
                "Password is too weak: password must include at least " + "one ";
        Exception passwordMustIncludeCapitalLetter =
                Assertions.assertThrows(PasswordIsTooWeakException.class,
                        () -> AuthUtils.isPasswordTooWeak("123456712309"));
        Exception passwordMustIncludeLowerLetter =
                Assertions.assertThrows(PasswordIsTooWeakException.class,
                        () -> AuthUtils.isPasswordTooWeak("A12123345678"));
        Exception passwordMustIncludeNumbers =
                Assertions.assertThrows(PasswordIsTooWeakException.class,
                        () -> AuthUtils.isPasswordTooWeak("Aaadwwdadwa"));
        Exception passwordMustIncludeSpecialCharacters =
                Assertions.assertThrows(PasswordIsTooWeakException.class,
                        () -> AuthUtils.isPasswordTooWeak("Aa1234567812"));
        Assertions.assertAll("Check passord weakness",
                () -> Assertions.assertEquals(passwordMustIncludeCapitalLetter.getMessage(),
                        weakPasswordError + "capital letter"),
                () -> Assertions.assertEquals(passwordMustIncludeLowerLetter.getMessage(),
                        weakPasswordError + "lower letter"),
                () -> Assertions.assertEquals(passwordMustIncludeNumbers.getMessage(),
                        weakPasswordError + "number"),
                () -> Assertions.assertEquals(passwordMustIncludeSpecialCharacters.getMessage(),
                        weakPasswordError + "special character"));
        Assertions.assertDoesNotThrow(() -> AuthUtils.isPasswordTooWeak("*3r9cv9OkF{su"));
    }

    @Test
    void testEmailValidity() {
        Exception emptyEmailException = Assertions.assertThrows(EmptyEmailException.class,
                () -> AuthUtils.checkEmailValidity(""));
        Exception incorectEmailException = Assertions.assertThrows(IncorectEmailException.class,
                () -> AuthUtils.checkEmailValidity("incorrectEmail@"));
        Assertions.assertDoesNotThrow(() -> AuthUtils.checkEmailValidity("email@service.com"));
    }
}
