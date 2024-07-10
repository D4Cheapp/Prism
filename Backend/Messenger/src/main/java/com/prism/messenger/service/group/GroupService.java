package com.prism.messenger.service.group;

import com.prism.messenger.exception.group.EmptyGroupNameException;
import com.prism.messenger.model.group.CreateGroupModel;
import com.prism.messenger.model.group.GroupModel;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface GroupService {

  GroupModel createGroup(String email, CreateGroupModel createGroupModel)
      throws IOException, EmptyGroupNameException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
