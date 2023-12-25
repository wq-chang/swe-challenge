package com.swe.challenge.service.impl;

import com.swe.challenge.exception.ResourceExistException;
import com.swe.challenge.exception.ResourceNotFoundException;
import com.swe.challenge.repository.SessionRepository;
import com.swe.challenge.repository.UserRepository;
import com.swe.challenge.repository.entity.Invitation;
import com.swe.challenge.repository.entity.Session;
import com.swe.challenge.repository.entity.User;
import com.swe.challenge.service.SessionCommandService;
import com.swe.challenge.service.mapper.SessionMapper;
import com.swe.challenge.service.model.InvitationResultDto;
import com.swe.challenge.service.model.SessionDto;
import com.swe.challenge.util.RandomUtils;
import java.util.ArrayList;
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
@Transactional
public class SessionCommandServiceImpl implements SessionCommandService {

  private final SessionRepository sessionRepository;
  private final UserRepository userRepository;

  @Override
  public SessionDto initiateSession(@NonNull String username)
      throws ResourceNotFoundException, ResourceExistException {
    val optOwner = userRepository.findByUsername(username);
    val owner = optOwner.orElseThrow(() -> new ResourceNotFoundException("user not found"));
    val hasActiveSession = sessionRepository.countByOwner_IdAndActive(owner.getId(), true) > 0;
    if (hasActiveSession) {
      throw new ResourceExistException("User already has one active session");
    }

    val newSession =
        Session.builder()
            .uuid(UUID.randomUUID())
            .owner(owner)
            .active(true)
            .invitations(new ArrayList<>())
            .build();
    val invitation =
        Invitation.builder()
            .uuid(UUID.randomUUID())
            .invitee(owner)
            .session(newSession)
            .accepted(true)
            .build();
    newSession.addInvitees(List.of(invitation));
    sessionRepository.save(newSession);

    return SessionMapper.INSTANCE.sessionToSessionDto(newSession);
  }

  @Override
  public InvitationResultDto inviteUsers(
      @NonNull String ownerUsername, @NonNull List<String> inviteeUsernames)
      throws ResourceNotFoundException {
    val optSession = sessionRepository.findByOwnerUsernameAndActive(ownerUsername, true);
    val session =
        optSession.orElseThrow(() -> new ResourceNotFoundException("User has no active session"));

    val existingUser = userRepository.findAllByUsernameIn(inviteeUsernames);
    val existingUsernames = existingUser.stream().map(User::getUsername).toList();
    val notFoundInviteeUsername =
        inviteeUsernames.stream().filter(u -> !existingUsernames.contains(u)).toList();

    val newInvitees = new ArrayList<Invitation>();
    val newInviteeUsernames = new ArrayList<String>();
    val existingInviteeUsernames = new ArrayList<String>();
    for (val user : existingUser) {
      if (session.containsUser(user)) {
        existingInviteeUsernames.add(user.getUsername());
      } else {
        newInviteeUsernames.add(user.getUsername());
        newInvitees.add(
            Invitation.builder().uuid(UUID.randomUUID()).invitee(user).session(session).build());
      }
    }

    if (!newInvitees.isEmpty()) {
      session.addInvitees(newInvitees);
      sessionRepository.save(session);
    }

    return InvitationResultDto.builder()
        .newInvitedUsers(newInviteeUsernames)
        .notFoundUsers(notFoundInviteeUsername)
        .invitedUsers(existingInviteeUsernames)
        .build();
  }

  @Override
  public SessionDto endSession(@NonNull String username) throws ResourceNotFoundException {
    val optSession = sessionRepository.findByOwnerUsernameAndActive(username, true);
    val session =
        optSession.orElseThrow(() -> new ResourceNotFoundException("User has no active session"));
    session.setActive(false);
    val invitationsWithRestaurant =
        session.getInvitations().stream()
            .filter(i -> StringUtils.isNotBlank(i.getRestaurant()))
            .toList();
    if (!invitationsWithRestaurant.isEmpty()) {
      val num = RandomUtils.nextInt(invitationsWithRestaurant.size());
      val pickedInvitation = invitationsWithRestaurant.get(num);
      pickedInvitation.setPicked(true);
      session.setPickedRestaurant(pickedInvitation.getRestaurant());
    }

    sessionRepository.save(session);
    return SessionMapper.INSTANCE.sessionToSessionDto(session);
  }
}
