package com.swe.challenge.controller;

import com.swe.challenge.exception.ResourceExistException;
import com.swe.challenge.service.UserCommandService;
import com.swe.challenge.service.model.RegisterUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** The type User command controller. */
@RestController
@RequiredArgsConstructor
@Validated
public class UserCommandController {

  private final UserCommandService userCommandService;

  /**
   * Register new user response entity.
   *
   * @param registerUserDto the register user dto
   * @return the response entity
   * @throws ResourceExistException the resource exist exception
   */
  @PostMapping("/users/register")
  public ResponseEntity<RegisterUserDto> registerNewUser(
      @RequestBody @Valid RegisterUserDto registerUserDto) throws ResourceExistException {
    return ResponseEntity.ok(userCommandService.registerNewUser(registerUserDto));
  }
}
