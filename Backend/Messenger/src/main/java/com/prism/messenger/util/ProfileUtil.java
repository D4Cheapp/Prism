package com.prism.messenger.util;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.service.minio.impl.MinioServiceImpl;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class ProfileUtil {
    @Autowired
    static private MinioServiceImpl minioService;

    public static Profile getProfileBy(String arg, CheckIsProfileExistFunction method)
            throws ProfileNotExistException {
        Optional<Profile> profile = method.method(arg);
        boolean isProfileNotFound = profile.isEmpty();
        if (isProfileNotFound) {
            throw new ProfileNotExistException();
        }
        return profile.get();
    }

    public static byte[] loadPictureInProfileModel(Profile profile)
            throws InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException, ServerException {
        boolean isProfilePictureNotFound = profile.getProfilePicturePath() == null;
        if (isProfilePictureNotFound) {
            return null;
        }
        return minioService.getFile(profile.getProfilePicturePath());
    }

    @FunctionalInterface
    public interface CheckIsProfileExistFunction {

        Optional<Profile> method(String arg);
    }
}