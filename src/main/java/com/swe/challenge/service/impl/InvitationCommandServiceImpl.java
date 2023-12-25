package com.swe.challenge.service.impl;

import com.swe.challenge.exception.ResourceExistException;
import com.swe.challenge.exception.ResourceNotFoundException;
import com.swe.challenge.repository.InvitationRepository;
import com.swe.challenge.service.InvitationCommandService;
import com.swe.challenge.service.mapper.InvitationMapper;
import com.swe.challenge.service.model.InvitationDto;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InvitationCommandServiceImpl implements InvitationCommandService {

  private final InvitationRepository invitationRepository;

  @Override
  public InvitationDto acceptInvitation(
      @NonNull String inviteeUsername, @NonNull UUID invitationUuid)
      throws ResourceNotFoundException {
    val optInvitation =
        invitationRepository.findByUuidAndInvitee_UsernameAndSession_ActiveAndAccepted(
            invitationUuid, inviteeUsername, true, false);
    val invitation =
        optInvitation.orElseThrow(
            () -> new ResourceNotFoundException("Pending invitation not found"));
    invitation.setAccepted(true);
    invitationRepository.save(invitation);

    return InvitationMapper.INSTANCE.invitationToInvitationDto(invitation);
  }

  @Override
  public InvitationDto suggestRestaurant(
      @NonNull String inviteeUsername, @NonNull UUID invitationUuid, @NonNull String restaurant)
      throws ResourceNotFoundException, ResourceExistException {
    val optInvitation =
        invitationRepository.findByUuidAndInvitee_UsernameAndSession_ActiveAndAccepted(
            invitationUuid, inviteeUsername, true, true);
    val invitation =
        optInvitation.orElseThrow(
            () -> new ResourceNotFoundException("Accepted invitation not found"));

    if (StringUtils.isNotBlank(invitation.getRestaurant())) {
      throw new ResourceExistException("User already suggest a restaurant");
    }
    invitation.setRestaurant(restaurant);
    invitationRepository.save(invitation);

    return InvitationMapper.INSTANCE.invitationToInvitationDto(invitation);
  }
}
