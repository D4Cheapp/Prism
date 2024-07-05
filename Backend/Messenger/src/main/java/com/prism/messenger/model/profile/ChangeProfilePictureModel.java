package com.prism.messenger.model.profile;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChangeProfilePictureModel {

  private MultipartFile picture;
}
