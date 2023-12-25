package com.swe.challenge.service;

import com.swe.challenge.exception.ResourceExistException;
import com.swe.challenge.exception.ResourceNotFoundException;
import com.swe.challenge.service.model.InvitationResultDto;
import com.swe.challenge.service.model.SessionDto;
import java.util.List;
import lombok.NonNull;

public interface SessionCommandService {

  /**
   * Initiate a session, 1 user is allowed to have only 1 active session.
   *
   * @param username the username
   * @return session info
   * @throws ResourceExistException user has an existing active session
   * @throws ResourceNotFoundException user not found
   */
  SessionDto initiateSession(@NonNull String username)
      throws ResourceExistException, ResourceNotFoundException;

  /**
   * Invite users invite to join owner active session
   *
   * @param ownerUsername the session owner username
   * @param inviteeUsernames the invitee usernames
   * @return new invitees' username, not found username, existing invitees' username
   * @throws ResourceNotFoundException owner has no active session
   */
  InvitationResultDto inviteUsers(
      @NonNull String ownerUsername, @NonNull List<String> inviteeUsernames)
      throws ResourceNotFoundException;

  /**
   * End the active session and randomly choose one suggested restaurant if there is any suggestion.
   *
   * @param username the username
   * @return session info
   * @throws ResourceNotFoundException user not found
   */
  SessionDto endSession(@NonNull String username) throws ResourceNotFoundException;
}
