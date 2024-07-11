package com.prism.messenger.model.dialog;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChangeGroupPhotoModel {

  private String groupId;
  private MultipartFile groupPhoto;
}
