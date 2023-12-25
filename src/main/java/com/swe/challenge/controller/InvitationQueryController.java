package com.swe.challenge.controller;

import com.swe.challenge.exception.ResourceNotFoundException;
import com.swe.challenge.repository.entity.User;
import com.swe.challenge.service.InvitationQueryService;
import com.swe.challenge.service.model.InvitationDto;
import com.swe.challenge.service.model.ListInvitationInputDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** The type Invitation query controller. */
@RestController
@RequiredArgsConstructor
@Validated
public class InvitationQueryController {

  private final InvitationQueryService invitationQueryService;

  /**
   * List invitations response entity.
   *
   * @param dto the dto
   * @return the response entity
   * @throws ResourceNotFoundException the resource not found exception
   */
  @PostMapping("/invitations/search")
  public ResponseEntity<List<InvitationDto>> listInvitations(
      @RequestBody @Valid ListInvitationInputDto dto) throws ResourceNotFoundException {
    return ResponseEntity.ok(
        invitationQueryService.listInvitation(
            dto.inviteeUsername(), dto.accepted(), dto.sessionActive()));
  }

  /**
   * List restaurants response entity.
   *
   * @param username the username
   * @param invitationUuid the invitation uuid
   * @return the response entity
   * @throws ResourceNotFoundException the resource not found exception
   */
  @GetMapping("/invitations/{username}/{invitationUuid}/restaurants")
  public ResponseEntity<List<String>> listRestaurants(
      @PathVariable @Valid @NotBlank @Size(max = User.Attributes.USERNAME_MAX_LENGTH)
          String username,
      @PathVariable @Valid @NotNull UUID invitationUuid)
      throws ResourceNotFoundException {
    return ResponseEntity.ok(invitationQueryService.listRestaurants(username, invitationUuid));
  }
}
