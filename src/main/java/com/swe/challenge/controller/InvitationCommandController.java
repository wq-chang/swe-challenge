package com.swe.challenge.controller;

import com.swe.challenge.exception.ResourceExistException;
import com.swe.challenge.exception.ResourceNotFoundException;
import com.swe.challenge.service.InvitationCommandService;
import com.swe.challenge.service.model.AcceptInvitationDto;
import com.swe.challenge.service.model.InvitationDto;
import com.swe.challenge.service.model.SuggestRestaurantDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** The type Invitation command controller. */
@RestController
@RequiredArgsConstructor
@Validated
public class InvitationCommandController {

  private final InvitationCommandService invitationCommandService;

  /**
   * Accept invitation
   *
   * @param dto the dto
   * @return the response entity
   * @throws ResourceNotFoundException the resource not found exception
   */
  @PostMapping("/invitations/accept")
  public ResponseEntity<InvitationDto> acceptInvitation(@RequestBody @Valid AcceptInvitationDto dto)
      throws ResourceNotFoundException {
    return ResponseEntity.ok(
        invitationCommandService.acceptInvitation(dto.inviteeUsername(), dto.invitationUuid()));
  }

  /**
   * Suggest restaurant response entity.
   *
   * @param dto the dto
   * @return the response entity
   * @throws ResourceNotFoundException the resource not found exception
   * @throws ResourceExistException the resource exist exception
   */
  @PostMapping("/invitations/suggest-restaurant")
  public ResponseEntity<InvitationDto> suggestRestaurant(
      @RequestBody @Valid SuggestRestaurantDto dto)
      throws ResourceNotFoundException, ResourceExistException {
    return ResponseEntity.ok(
        invitationCommandService.suggestRestaurant(
            dto.inviteeUsername(), dto.invitationUuid(), dto.restaurant()));
  }
}
