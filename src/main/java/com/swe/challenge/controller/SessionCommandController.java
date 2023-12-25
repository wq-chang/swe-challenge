package com.swe.challenge.controller;

import com.swe.challenge.exception.ResourceExistException;
import com.swe.challenge.exception.ResourceNotFoundException;
import com.swe.challenge.service.SessionCommandService;
import com.swe.challenge.service.model.InitiateEndSessionInputDto;
import com.swe.challenge.service.model.InvitationResultDto;
import com.swe.challenge.service.model.InviteInputDto;
import com.swe.challenge.service.model.SessionDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** The type Session command controller. */
@RestController
@RequiredArgsConstructor
@Validated
public class SessionCommandController {

  private final SessionCommandService sessionCommandService;

  /**
   * Initiate new session response entity.
   *
   * @param dto the dto
   * @return the response entity
   * @throws ResourceExistException the resource exist exception
   * @throws ResourceNotFoundException the resource not found exception
   */
  @PostMapping("/sessions/initiate")
  public ResponseEntity<SessionDto> initiateNewSession(
      @RequestBody @Valid InitiateEndSessionInputDto dto)
      throws ResourceExistException, ResourceNotFoundException {
    return ResponseEntity.ok(sessionCommandService.initiateSession(dto.username()));
  }

  /**
   * Invite users response entity.
   *
   * @param dto the dto
   * @return the response entity
   * @throws ResourceNotFoundException the resource not found exception
   */
  @PostMapping("/sessions/invite-users")
  public ResponseEntity<InvitationResultDto> inviteUsers(@RequestBody @Valid InviteInputDto dto)
      throws ResourceNotFoundException {
    return ResponseEntity.ok(
        sessionCommandService.inviteUsers(dto.ownerUsername(), dto.inviteeUsernames()));
  }

  /**
   * End session response entity.
   *
   * @param dto the dto
   * @return the response entity
   * @throws ResourceNotFoundException the resource not found exception
   */
  @PostMapping("/sessions/end")
  public ResponseEntity<SessionDto> endSession(@RequestBody @Valid InitiateEndSessionInputDto dto)
      throws ResourceNotFoundException {
    return ResponseEntity.ok(sessionCommandService.endSession(dto.username()));
  }
}
