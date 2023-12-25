package com.swe.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InvitationQueryServiceImplTest {

  @Autowired private InvitationQueryService invitationQueryService;

  private static final String OWNER_USERNAME = "username";
  private static final UUID SESSION_UUID = UUID.randomUUID();
  private static final UUID SESSION_B_UUID = UUID.randomUUID();
  private static final UUID INVITEE_B_UUID = UUID.randomUUID();
  private static final String INVITEE_USERNAME_A = "inviteeA";
  private static final String INVITEE_USERNAME_B = "inviteeB";
  private static final UUID ACCEPTED_INVITEE_UUID = UUID.randomUUID();
  private static final String RESTAURANT_A = "A";
  private static final String RESTAURANT_B = "B";

  @BeforeAll
  static void setup(
      @Autowired UserRepository userRepository, @Autowired SessionRepository sessionRepository) {
    val owner = UserUtility.buildUser(OWNER_USERNAME);
    val userA = UserUtility.buildUser(INVITEE_USERNAME_A);
    val userB = UserUtility.buildUser(INVITEE_USERNAME_B);
    val userC = UserUtility.buildUser("user c");
    userRepository.saveAll(List.of(owner, userA, userB, userC));

    val sessionA = SessionUtility.buildSession(owner, true, SESSION_UUID);
    val inviteeA = InvitationUtility.buildInvitation(sessionA, userA, false);
    val inviteeB = InvitationUtility.buildInvitation(sessionA, userB, false, INVITEE_B_UUID);
    sessionA.addInvitees(List.of(inviteeA, inviteeB));

    val sessionB = SessionUtility.buildSession(userA, true, SESSION_B_UUID);
    val inviteeSessionB =
        InvitationUtility.buildInvitation(
            sessionB, userB, true, ACCEPTED_INVITEE_UUID, RESTAURANT_A);
    val invitationBSessionB =
        InvitationUtility.buildInvitation(sessionB, userC, true, UUID.randomUUID(), RESTAURANT_B);
    sessionB.addInvitees(List.of(inviteeSessionB, invitationBSessionB));
    sessionRepository.saveAll(List.of(sessionA, sessionB));
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
  void listInvitation_FindPendingHasPending_ReturnPendingInvitation()
      throws ResourceNotFoundException {
    val result =
        invitationQueryService.listInvitation(INVITEE_USERNAME_B, List.of(false), List.of(true));

    assertEquals(1, result.size());
    val invitationDto = result.getFirst();
    assertEquals(SESSION_UUID, invitationDto.sessionUuid());
    assertEquals(INVITEE_B_UUID, invitationDto.invitationUuid());
    assertEquals(OWNER_USERNAME, invitationDto.ownerUsername());
  }

  @Test
  void listInvitation_FindPendingButNoPendingInvitation_ReturnEmptyList()
      throws ResourceNotFoundException {
    val result =
        invitationQueryService.listInvitation(OWNER_USERNAME, List.of(false), List.of(true));

    assertTrue(result.isEmpty());
  }

  @Test
  void listInvitation_FindAcceptedHasAccepted_ReturnAcceptedInvitation()
      throws ResourceNotFoundException {
    val result =
        invitationQueryService.listInvitation(
            INVITEE_USERNAME_B, List.of(true), List.of(true, false));

    assertEquals(1, result.size());
    val invitationDto = result.getFirst();
    assertEquals(ACCEPTED_INVITEE_UUID, invitationDto.invitationUuid());
    assertEquals(INVITEE_USERNAME_A, invitationDto.ownerUsername());
  }

  @Test
  void listPendingInvitation_UsernameNotFound_ThrowResourceNotFound() {
    assertThrows(
        ResourceNotFoundException.class,
        () -> invitationQueryService.listInvitation("a", List.of(false), List.of(true)));
  }

  @Test
  void listRestaurants_HasRestaurants_ReturnList() throws ResourceNotFoundException {
    val result = invitationQueryService.listRestaurants(INVITEE_USERNAME_B, ACCEPTED_INVITEE_UUID);

    assertEquals(2, result.size());
    assertTrue(result.contains(RESTAURANT_A));
    assertTrue(result.contains(RESTAURANT_B));
  }
}
