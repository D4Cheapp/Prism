package com.prism.messenger.service.profile.impl;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.profile.CreateProfileException;
import com.prism.messenger.exception.profile.DeleteUserProfileException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.model.profile.FullProfileInfoModel;
import com.prism.messenger.repository.ProfileRepository;
import com.prism.messenger.service.minio.impl.MinioServiceImpl;
import com.prism.messenger.service.profile.ProfileService;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

  @Autowired
  private ProfileRepository profileRepository;
  @Autowired
  private MinioServiceImpl minioService;

  public FullProfileInfoModel getCurrentProfile(String email)
      throws ProfileNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    Optional<Profile> profile = profileRepository.findByEmail(email);
    boolean isProfileNotFound = profile.isEmpty();
    if (isProfileNotFound) {
      throw new ProfileNotExistException();
    }
    boolean isProfilePictureNotFound = profile.get().getProfilePicturePath() == null;
    if (isProfilePictureNotFound) {
      return new FullProfileInfoModel(profile, null);
    }
    return new FullProfileInfoModel(profile,
        minioService.getFile(profile.get().getProfilePicturePath()));
  }

  public void createProfile(String email) throws CreateProfileException {
    try {
      Profile profile = new Profile();
      profile.setEmail(email);
      profile.setTag("@user" + email.hashCode());
      profile.setName("username" + email.hashCode());
      profile.setLastOnlineTime(new Date().getTime());
      profileRepository.save(profile);
      minioService.createFolder("profiles/" + email + "/");
    } catch (Exception e) {
      throw new CreateProfileException();
    }
  }

  public void deleteProfile(String email) throws DeleteUserProfileException {
    try {
      Optional<Profile> profile = profileRepository.findByEmail(email);
      boolean isProfileNotFound = profile.isEmpty();
      if (isProfileNotFound) {
        throw new DeleteUserProfileException();
      }
      profileRepository.deleteById(profile.get().getTag());
      minioService.deleteFolder("profiles/" + email + "/");
    } catch (Exception e) {
      throw new DeleteUserProfileException();
    }
  }
}
