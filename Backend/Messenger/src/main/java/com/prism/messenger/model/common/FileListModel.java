package com.prism.messenger.model.common;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileListModel {

  private Integer totalCount;
  private List<byte[]> files;
}
