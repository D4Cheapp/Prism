package com.messenger.prism.service.auth.impl;

import com.messenger.prism.entity.Auth;
import com.messenger.prism.exception.auth.ActivationCodeExpireException;
import com.messenger.prism.model.auth.ActivationCodeModel;
import com.messenger.prism.repository.ActicationCodeRepo;
import com.messenger.prism.service.auth.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    @Autowired
    private ActicationCodeRepo acticationCodeRepo;
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void saveActivationCode(Auth account, String message, String activationUrl) {
        String code = UUID.randomUUID().toString();
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(fromEmail);
        mail.setTo(account.getEmail());
        mail.setSubject("Prism activation code");
        mail.setText(message + "\nActivation link: {backendurl/frontendurl}" + activationUrl + code);
        acticationCodeRepo.saveCode(new ActivationCodeModel(account, code));
        javaMailSender.send(mail);
    }

    public ActivationCodeModel getUserByActivationCode(String code) throws ActivationCodeExpireException {
        return acticationCodeRepo.findByActivationCode(code);
    }

    public void deleteActivationCode(String code) {
        acticationCodeRepo.deleteCode(code);
    }
}
