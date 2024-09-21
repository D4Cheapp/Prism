package com.prism.messenger.service.profile;

import com.prism.messenger.exception.EmptyParameterException;
import com.prism.messenger.exception.profile.*;
import com.prism.messenger.model.profile.ProfileModel;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface ChangeProfileInfoService {

    void changeProfileEmail(Object message) throws ChangeProfileEmailException;

    ProfileModel changeProfileName(String email, String name)
            throws ProfileNotExistException, ProfileNameIsTooLongException, ServerException,
            InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException;

    ProfileModel changeProfilePhoneNumber(String email, String phoneNumber)
            throws ProfileNotExistException, PhoneNumberAlreadyExistException,
            IncorrectPhoneNumberException, ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException;

    ProfileModel changeProfileTag(String email, String newTag)
            throws ProfileNotExistException, TagAlreadyExistException, ServerException,
            InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException;

    ProfileModel changeProfileStatus(String email, String status)
            throws ProfileNotExistException, StatusIsTooLongException, EmptyParameterException,
            ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException;

    ProfileModel changeProfilePicture(String email, MultipartFile picture)
            throws ProfileNotExistException, IOException, ServerException,
            InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
