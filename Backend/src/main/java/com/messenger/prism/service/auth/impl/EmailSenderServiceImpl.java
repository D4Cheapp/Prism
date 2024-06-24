package com.messenger.prism.service.auth.impl;

import com.messenger.prism.entity.Auth;
import com.messenger.prism.exception.PermissionsException;
import com.messenger.prism.exception.auth.ActivationCodeExpireException;
import com.messenger.prism.exception.auth.UserAlreadyExistException;
import com.messenger.prism.exception.auth.UserNotFoundException;
import com.messenger.prism.exception.auth.email.EmptyEmailException;
import com.messenger.prism.exception.auth.email.IncorectEmailException;
import com.messenger.prism.exception.auth.password.*;
import com.messenger.prism.model.auth.ActivationCodeModel;
import com.messenger.prism.model.auth.EmailModel;
import com.messenger.prism.model.auth.UserRegistrationModel;
import com.messenger.prism.repository.ActicationCodeRepo;
import com.messenger.prism.repository.AuthRepo;
import com.messenger.prism.service.auth.EmailSenderService;
import com.messenger.prism.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    @Autowired
    private AuthRepo storeUserRepo;
    @Autowired
    private ActicationCodeRepo acticationCodeRepo;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private PasswordEncoder encoder;
    @Value("${spring.mail.username}")
    private String fromEmail;

    public void saveActivationCode(Auth account, String message) {
        String code = UUID.randomUUID().toString();
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(fromEmail);
        mail.setTo(account.getEmail());
        mail.setSubject("Prism activation code");
        mail.setText(message + "\n Code: " + code);
        acticationCodeRepo.saveCode(new ActivationCodeModel(account, code));
        javaMailSender.send(mail);
    }

    public ActivationCodeModel getUserByActivationCode(String code) throws ActivationCodeExpireException {
        return acticationCodeRepo.findByActivationCode(code);
    }

    public void deleteActivationCode(String code) {
        acticationCodeRepo.deleteCode(code);
    }


    public void sendEditUserEmailCode(Authentication authentication, EmailModel editData) throws PermissionsException, UserNotFoundException, UserAlreadyExistException, EmptyEmailException, IncorectEmailException {
        Optional<Auth> storedUser = storeUserRepo.findById(editData.getId());
        Auth changedEmailUser = storeUserRepo.findByEmail(editData.getEmail());
        boolean isUserNotFound = storedUser.isEmpty();
        boolean isLoginAlreadyExists = changedEmailUser != null;
        AuthUtils.checkPermission(authentication, storedUser, storeUserRepo);
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        if (isLoginAlreadyExists) {
            throw new UserAlreadyExistException();
        }
        AuthUtils.checkEmailValidity(editData.getEmail());
        storedUser.get().setEmail(editData.getEmail());
        this.saveActivationCode(storedUser.get(),
                "Activation code for confirm " + "your" + " " + "email ");
    }

    public void sendRegitrationCode(UserRegistrationModel user) throws IncorrectConfirmPasswordException, UserAlreadyExistException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, EmptyEmailException, IncorectEmailException {
        boolean isDeveloperFieldMissing = user.getIsDeveloper() == null;
        boolean isUserAlreadyExists = storeUserRepo.findByEmail(user.getEmail()) != null;
        boolean isConfirmPasswordInorrect =
                !(user.getConfirmPassword() != null && user.getConfirmPassword().equals(user.getPassword()));
        if (isDeveloperFieldMissing) {
            user.setIsDeveloper(false);
        }
        if (isUserAlreadyExists) {
            throw new UserAlreadyExistException();
        }
        if (isConfirmPasswordInorrect) {
            throw new IncorrectConfirmPasswordException();
        }
        AuthUtils.checkEmailValidity(user.getEmail());
        AuthUtils.checkPasswordValidity(user.getPassword());
        Auth userEntity = UserRegistrationModel.toEntity(user, encoder);
        this.saveActivationCode(userEntity, "Activation code for registration");
    }

    public void sendRestorePasswordCode(String email) throws UserNotFoundException {
        Auth currentUser = storeUserRepo.findByEmail(email);
        boolean isUserNotFound = currentUser == null;
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        this.saveActivationCode(currentUser, "Restore password code");
    }
}
