package com.prism.messenger.util;

import java.util.Optional;
import java.util.UUID;

public class DialogUtils {

  public static String generateDialogId(CheckUniqueIdFunction repository) {
    boolean isUniqueId = false;
    UUID uniqueChatId = null;
    while (!isUniqueId) {
      UUID chatId = UUID.randomUUID();
      Optional<Boolean> isDialogIdUnique = repository.method(chatId);
      isUniqueId = isDialogIdUnique.isPresent() && isDialogIdUnique.get();
      if (isUniqueId) {
        uniqueChatId = chatId;
      }
    }
    return uniqueChatId.toString();
  }

  @FunctionalInterface
  public interface CheckUniqueIdFunction {

    Optional<Boolean> method(UUID chatId);
  }
}
