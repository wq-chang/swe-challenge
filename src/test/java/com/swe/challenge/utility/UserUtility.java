package com.swe.challenge.utility;

import com.swe.challenge.repository.entity.User;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class UserUtility {
  public static User buildUser(String username) {
    return User.builder().uuid(UUID.randomUUID()).username(username).displayName(username).build();
  }
}
