package com.prism.messenger.service.profile;

import com.prism.messenger.exception.EmptyParameterException;
import com.prism.messenger.exception.profile.ChangeProfileEmailException;
import com.prism.messenger.exception.profile.IncorrectPhoneNumberException;
import com.prism.messenger.exception.profile.PhoneNumberAlreadyExistException;
import com.prism.messenger.exception.profile.ProfileNameIsTooLongException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.exception.profile.StatusIsTooLongException;
import com.prism.messenger.exception.profile.TagAlreadyExistException;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.springframework.web.multipart.MultipartFile;

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

  void deleteProfilePicture(String email)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, ProfileNotExistException;
}
