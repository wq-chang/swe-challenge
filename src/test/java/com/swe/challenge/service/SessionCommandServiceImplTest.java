package com.swe.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SessionCommandServiceImplTest {

  @Autowired private SessionCommandService sessionCommandService;
  @Autowired private SessionRepository sessionRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private InvitationRepository invitationRepository;

  private static final String USERNAME = "username";

  @BeforeEach
  void setup() {
    val user = UserUtility.buildUser(USERNAME);
    userRepository.save(user);
  }

  @AfterEach
  void clean() {
    invitationRepository.deleteAll();
    sessionRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void initiateSession_Valid_Success() throws ResourceExistException, ResourceNotFoundException {
    val result = sessionCommandService.initiateSession(USERNAME);

    assertNotNull(result.uuid());
    assertEquals(USERNAME, result.ownerDisplayName());
  }

  @Test
  void initiateSession_UserNotFound_ThrowResourceNotFound() {
    assertThrows(ResourceNotFoundException.class, () -> sessionCommandService.initiateSession("a"));
  }

  @Test
  void initiateSession_HasExistingActiveSession_ThrowResourceExist()
      throws ResourceExistException, ResourceNotFoundException {
    sessionCommandService.initiateSession(USERNAME);

    assertThrows(
        ResourceExistException.class, () -> sessionCommandService.initiateSession(USERNAME));
  }

  @Test
  void inviteUsers_Valid_Success() throws ResourceExistException, ResourceNotFoundException {
    sessionCommandService.initiateSession(USERNAME);
    val nameA = "a";
    val nameB = "b";
    val nameC = "c";
    val nameD = "d";
    val nameE = "e";
    val nameF = "f";
    val userA = UserUtility.buildUser(nameA);
    val userB = UserUtility.buildUser(nameB);
    val userC = UserUtility.buildUser(nameC);
    val userD = UserUtility.buildUser(nameD);
    userRepository.saveAll(List.of(userA, userB, userC, userD));

    val result =
        sessionCommandService.inviteUsers(
            USERNAME, List.of(nameA, nameB, nameC, nameD, nameE, nameF));
    val newInvitees = result.newInvitedUsers();
    val notFoundUsers = result.notFoundUsers();
    assertEquals(0, result.invitedUsers().size());
    assertEquals(2, notFoundUsers.size());
    assertEquals(nameE, notFoundUsers.getFirst());
    assertEquals(nameF, notFoundUsers.getLast());
    assertEquals(4, newInvitees.size());
    assertEquals(nameA, newInvitees.getFirst());
    assertEquals(nameB, newInvitees.get(1));
    assertEquals(nameC, newInvitees.get(2));
    assertEquals(nameD, newInvitees.getLast());

    val result2 = sessionCommandService.inviteUsers(USERNAME, List.of(nameA, nameB));
    val invitedUser = result2.invitedUsers();
    assertEquals(2, invitedUser.size());
    assertEquals(nameA, invitedUser.getFirst());
    assertEquals(nameB, invitedUser.getLast());

    val optSession = sessionRepository.findByOwnerUsernameAndActive(USERNAME, true);
    assertTrue(optSession.isPresent());
    val session = optSession.get();
    assertEquals(5, session.getInvitations().size());
  }

  @Test
  void inviteUsers_NoActiveSession_ThrowResourceNotFound() {
    val nameA = "a";
    val nameB = "b";
    assertThrows(
        ResourceNotFoundException.class,
        () -> sessionCommandService.inviteUsers(USERNAME, List.of(nameA, nameB)));
  }

  @ParameterizedTest
  @ValueSource(strings = {"restaurant"})
  @NullSource
  void endSession_SuggestedRestaurant_PickedRestaurant(String restaurant)
      throws ResourceNotFoundException {
    val ownerUsername = "abc";
    val owner = UserUtility.buildUser(ownerUsername);
    val invitee = UserUtility.buildUser("invitee");
    userRepository.saveAll(List.of(owner, invitee));
    val sessionUuid = UUID.randomUUID();
    val session = SessionUtility.buildSession(owner, true, sessionUuid);
    val invitationUuid = UUID.randomUUID();
    val invitation =
        InvitationUtility.buildInvitation(session, invitee, true, invitationUuid, restaurant);
    session.addInvitees(List.of(invitation));
    sessionRepository.save(session);

    val result = sessionCommandService.endSession(ownerUsername);

    assertEquals(sessionUuid, result.uuid());
    assertFalse(result.active());
    assertEquals(restaurant, result.pickedRestaurant());
  }
}
