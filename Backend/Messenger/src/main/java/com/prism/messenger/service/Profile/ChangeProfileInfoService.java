package com.prism.messenger.service.Profile;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.FileIsEmptyException;
import com.prism.messenger.exception.PermissionsException;
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

  Profile changeProfileName(String email, String name)
      throws ProfileNotExistException, ProfileNameIsTooLongException;

  Profile changeProfilePhoneNumber(String email, String phoneNumber)
      throws ProfileNotExistException, PhoneNumberAlreadyExistException, IncorrectPhoneNumberException;

  Profile changeProfileTag(String email, String newTag)
      throws ProfileNotExistException, TagAlreadyExistException, PermissionsException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

  Profile changeProfileStatus(String email, String status)
      throws ProfileNotExistException, StatusIsTooLongException;

  Profile changeProfilePicture(String email, MultipartFile picture)
      throws FileIsEmptyException, ProfileNotExistException, IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
