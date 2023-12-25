package com.swe.challenge.utility;

import com.swe.challenge.repository.entity.Session;
import com.swe.challenge.repository.entity.User;
import java.util.ArrayList;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class SessionUtility {

  public static Session buildSession(User owner, boolean active) {
    return buildSession(owner, active, UUID.randomUUID());
  }

  public static Session buildSession(User owner, boolean active, UUID uuid) {
    return Session.builder()
        .uuid(uuid)
        .owner(owner)
        .active(active)
        .invitations(new ArrayList<>())
        .build();
  }
}
