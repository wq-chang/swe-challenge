package com.swe.challenge.service;

import com.swe.challenge.exception.ResourceNotFoundException;
import com.swe.challenge.service.model.InvitationDto;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;

public interface InvitationQueryService {

  /**
   * List pending invitation list.
   *
   * @param username invitee username
   * @param accepted list of accepted boolean
   * @param active list of session active boolean
   * @return the invitation list
   * @throws ResourceNotFoundException user not found
   */
  List<InvitationDto> listInvitation(
      @NonNull String username, @NonNull List<Boolean> accepted, @NonNull List<Boolean> active)
      throws ResourceNotFoundException;

  /**
   * List submitted restaurants by session.
   *
   * @param username the current user username
   * @param invitationUuid the invitation uuid
   * @return the restaurants
   */
  List<String> listRestaurants(@NonNull String username, @NonNull UUID invitationUuid)
      throws ResourceNotFoundException;
}
