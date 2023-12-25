package com.swe.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.swe.challenge.exception.ResourceExistException;
import com.swe.challenge.exception.ResourceNotFoundException;
import com.swe.challenge.repository.InvitationRepository;
import com.swe.challenge.repository.SessionRepository;
import com.swe.challenge.repository.UserRepository;
import com.swe.challenge.utility.InvitationUtility;
import com.swe.challenge.utility.SessionUtility;
import com.swe.challenge.utility.UserUtility;
import java.util.List;
import java.util.UUID;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InvitationCommandServiceImplTest {

  @Autowired private InvitationCommandService invitationCommandService;
  @Autowired private InvitationRepository invitationRepository;

  private static final String OWNER_USERNAME = "owner";
  private static final String INVITEE_USERNAME_A = "inviteeA";
  private static final UUID INVITATION_UUID = UUID.randomUUID();

  @BeforeAll
  static void setup(
      @Autowired UserRepository userRepository, @Autowired SessionRepository sessionRepository) {
    val owner = UserUtility.buildUser(OWNER_USERNAME);
    val userA = UserUtility.buildUser(INVITEE_USERNAME_A);
    userRepository.saveAll(List.of(owner, userA));
    val session = SessionUtility.buildSession(owner, true);
    val invitation = InvitationUtility.buildInvitation(session, userA, false, INVITATION_UUID);
    session.addInvitees(List.of(invitation));
    sessionRepository.save(session);
  }

  @BeforeEach
  void setup() {
    val invitations = invitationRepository.findAll();
    invitations.forEach(
        i -> {
          i.setAccepted(false);
          i.setRestaurant(null);
        });
    invitationRepository.saveAll(invitations);
  }

  @AfterAll
  static void clean(
      @Autowired UserRepository userRepository,
      @Autowired SessionRepository sessionRepository,
      @Autowired InvitationRepository invitationRepository) {
    invitationRepository.deleteAll();
    sessionRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void acceptInvitation_Valid_Success() throws ResourceNotFoundException {
    val result = invitationCommandService.acceptInvitation(INVITEE_USERNAME_A, INVITATION_UUID);

    assertEquals(OWNER_USERNAME, result.ownerUsername());
    assertEquals(INVITATION_UUID, result.invitationUuid());
    assertTrue(result.accepted());
    assertTrue(result.sessionActive());
  }

  @Test
  void acceptInvitation_PendingInvitationNotFound_ThrowResourceNotFound() {
    assertThrows(
        ResourceNotFoundException.class,
        () -> invitationCommandService.acceptInvitation(INVITEE_USERNAME_A, UUID.randomUUID()));
  }

  @Test
  void suggestRestaurant_Valid_Success() throws ResourceNotFoundException, ResourceExistException {
    invitationCommandService.acceptInvitation(INVITEE_USERNAME_A, INVITATION_UUID);
    val restaurant = "restaurant";
    val result =
        invitationCommandService.suggestRestaurant(INVITEE_USERNAME_A, INVITATION_UUID, restaurant);

    assertEquals(OWNER_USERNAME, result.ownerUsername());
    assertEquals(INVITATION_UUID, result.invitationUuid());
    assertTrue(result.accepted());
    assertTrue(result.sessionActive());
    assertEquals(restaurant, result.restaurant());
  }

  @Test
  void suggestRestaurant_AlreadySuggestedRestaurant_FailedToSuggestNewRestaurant()
      throws ResourceNotFoundException, ResourceExistException {
    invitationCommandService.acceptInvitation(INVITEE_USERNAME_A, INVITATION_UUID);
    val restaurant = "restaurant";
    invitationCommandService.suggestRestaurant(INVITEE_USERNAME_A, INVITATION_UUID, restaurant);
    assertThrows(
        ResourceExistException.class,
        () ->
            invitationCommandService.suggestRestaurant(
                INVITEE_USERNAME_A, INVITATION_UUID, restaurant));
  }

  @Test
  void suggestRestaurant_AcceptedInvitationNotFound_ThrowResourceNotFound() {
    assertThrows(
        ResourceNotFoundException.class,
        () -> invitationCommandService.suggestRestaurant(INVITEE_USERNAME_A, INVITATION_UUID, "a"));
  }
}
