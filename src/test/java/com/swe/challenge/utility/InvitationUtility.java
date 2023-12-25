package com.swe.challenge.utility;

import com.swe.challenge.repository.entity.Invitation;
import com.swe.challenge.repository.entity.Session;
import com.swe.challenge.repository.entity.User;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class InvitationUtility {

  public static Invitation buildInvitation(Session session, User invitee, boolean accepted) {
    return buildInvitation(session, invitee, accepted, UUID.randomUUID(), null);
  }

  public static Invitation buildInvitation(
      Session session, User invitee, boolean accepted, UUID uuid) {
    return buildInvitation(session, invitee, accepted, uuid, null);
  }

  public static Invitation buildInvitation(
      Session session, User invitee, boolean accepted, UUID uuid, String restaurant) {
    return Invitation.builder()
        .uuid(uuid)
        .session(session)
        .invitee(invitee)
        .accepted(accepted)
        .restaurant(restaurant)
        .build();
  }
}
