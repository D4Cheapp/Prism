package com.prism.messenger.model;

import lombok.Data;

@Data
public class DialogFilesPaginationListModel {

  private String dialogId;
  private Integer page;
  private Integer size;
}
