package com.swe.challenge.service.impl;

import com.swe.challenge.exception.ResourceNotFoundException;
import com.swe.challenge.repository.InvitationRepository;
import com.swe.challenge.repository.UserRepository;
import com.swe.challenge.repository.entity.Invitation;
import com.swe.challenge.service.InvitationQueryService;
import com.swe.challenge.service.model.InvitationDto;
import java.util.List;
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
@Transactional(readOnly = true)
public class InvitationQueryServiceImpl implements InvitationQueryService {

  private final UserRepository userRepository;
  private final InvitationRepository invitationRepository;

  @Override
  public List<InvitationDto> listInvitation(
      @NonNull String username, @NonNull List<Boolean> accepted, @NonNull List<Boolean> active)
      throws ResourceNotFoundException {
    val optUser = userRepository.findByUsername(username);
    val user = optUser.orElseThrow(() -> new ResourceNotFoundException("User not found"));

    return invitationRepository.findAllByInvitationAndAcceptedInAndSession_ActiveIn(
        user.getId(), accepted, active);
  }

  @Override
  public List<String> listRestaurants(@NonNull String username, @NonNull UUID invitationUuid)
      throws ResourceNotFoundException {
    val optInvitation =
        invitationRepository.findByUuidAndInvitee_UsernameAndAccepted(
            invitationUuid, username, true);
    val invitation =
        optInvitation.orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));
    return invitation.getSession().getInvitations().stream()
        .map(Invitation::getRestaurant)
        .filter(StringUtils::isNotBlank)
        .toList();
  }
}
