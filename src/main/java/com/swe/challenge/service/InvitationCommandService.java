package com.swe.challenge.service;

import com.swe.challenge.exception.ResourceExistException;
import com.swe.challenge.exception.ResourceNotFoundException;
import com.swe.challenge.service.model.InvitationDto;
import java.util.UUID;
import lombok.NonNull;

public interface InvitationCommandService {

  /**
   * Accept invitation.
   *
   * @param inviteeUsername the invitee username
   * @param invitationUuid the invitee uuid
   * @return the invitation result
   * @throws ResourceNotFoundException pending invitation not found
   */
  InvitationDto acceptInvitation(@NonNull String inviteeUsername, @NonNull UUID invitationUuid)
      throws ResourceNotFoundException;

  /**
   * Suggest restaurant invitation dto.
   *
   * @param inviteeUsername the invitee username
   * @param invitationUuid the invitation uuid
   * @param restaurant the restaurant
   * @return the invitation info
   * @throws ResourceNotFoundException accepted invitation not found
   */
  InvitationDto suggestRestaurant(
      @NonNull String inviteeUsername, @NonNull UUID invitationUuid, @NonNull String restaurant)
      throws ResourceNotFoundException, ResourceExistException;
}
