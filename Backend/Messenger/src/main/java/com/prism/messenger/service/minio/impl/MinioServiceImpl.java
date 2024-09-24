package com.prism.messenger.service.minio.impl;

import com.google.common.collect.Iterables;
import com.prism.messenger.entity.Profile;
import com.prism.messenger.model.common.FileListModel;
import com.prism.messenger.service.minio.MinioService;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MinioServiceImpl implements MinioService {

  @Autowired
  private MinioClient minioClient;
  @Value("${spring.minio.bucket}")
  private String bucketName;

  public void deleteFolder(String path)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    Iterable<Result<Item>> objectsList = minioClient.listObjects(
        ListObjectsArgs.builder().bucket(bucketName).prefix(path + "/").build());
    for (Result<Item> result : objectsList) {
      Item item = result.get();
      minioClient.removeObject(
          RemoveObjectArgs.builder().bucket(bucketName).object(item.objectName()).build());
    }
  }

  public void deleteFile(String path)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(path).build());
  }

  public void createFolder(String path)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(path + "/")
        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1).build());
  }

  public void copyFromFolder(String from, String to)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    Iterable<Result<Item>> objectsList = minioClient.listObjects(
        ListObjectsArgs.builder().bucket(bucketName).prefix(from + "/").build());
    for (Result<Item> result : objectsList) {
      Item item = result.get();
      String[] parts = item.objectName().split("/");
      String fileName = parts[parts.length - 1];
      byte[] bytes = minioClient.getObject(
              GetObjectArgs.builder().bucket(bucketName).object(item.objectName()).build())
          .readAllBytes();
      minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(to + "/" + fileName)
          .stream(new ByteArrayInputStream(bytes), 0, -1).build());
    }
  }

  public void addFile(String path, MultipartFile file)
      throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(path)
        .stream(file.getInputStream(), file.getSize(), -1).build());
  }

  public byte[] getFile(String path)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(path).build())
        .readAllBytes();
  }

  public FileListModel getDialogFiles(String dialogPath, Integer page, Integer size)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    Iterable<Result<Item>> objectsList = minioClient.listObjects(
        ListObjectsArgs.builder().bucket(bucketName).prefix(dialogPath + "/").build());
    int totalCount = Iterables.size(objectsList);
    int iterCount = 0;
    List<byte[]> fileList = new ArrayList<>();
    for (Result<Item> result : objectsList) {
      iterCount++;
      boolean isNeededToParse = iterCount > page * size && iterCount <= (page * size) + size;
      boolean isLast = iterCount == (page * size) + size;
      if (isNeededToParse) {
        Item item = result.get();
        String[] parts = item.objectName().split("/");
        String fileName = parts[parts.length - 1];
        boolean isGroupPicture = fileName.equals("groupPicture.jpg");
        if (isGroupPicture) {
          continue;
        }
        byte[] bytes = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucketName).object(item.objectName()).build())
            .readAllBytes();
        fileList.add(bytes);
      }
      if (isLast) {
        break;
      }
    }
    return new FileListModel(totalCount - 1, fileList.subList(1, fileList.size()));
  }

  public byte[] loadPictureInProfileModel(Profile profile)
      throws InsufficientDataException, ErrorResponseException, IOException,
      NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
      XmlParserException, InternalException, ServerException {
    boolean isProfilePictureNotFound = profile.getProfilePicturePath() == null;
    if (isProfilePictureNotFound) {
      return null;
    }
    return this.getFile(profile.getProfilePicturePath());
  }
}
