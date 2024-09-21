package com.prism.messenger.service.profile;

import com.prism.messenger.exception.EmptyParameterException;
import com.prism.messenger.exception.profile.*;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface ChangeProfileInfoService {

    void changeProfileEmail(Object message) throws ChangeProfileEmailException;

    void changeProfileName(String email, String name)
            throws ProfileNotExistException, ProfileNameIsTooLongException, ServerException,
            InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException;

    void changeProfilePhoneNumber(String email, String phoneNumber)
            throws ProfileNotExistException, PhoneNumberAlreadyExistException,
            IncorrectPhoneNumberException, ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException;

    void changeProfileTag(String email, String newTag)
            throws ProfileNotExistException, TagAlreadyExistException, ServerException,
            InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException;

    void changeProfileStatus(String email, String status)
            throws ProfileNotExistException, StatusIsTooLongException, EmptyParameterException,
            ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException;

    void changeProfilePicture(String email, MultipartFile picture)
            throws ProfileNotExistException, IOException, ServerException,
            InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
