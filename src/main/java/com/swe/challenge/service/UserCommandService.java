package com.swe.challenge.service;

import com.swe.challenge.exception.ResourceExistException;
import com.swe.challenge.service.model.RegisterUserDto;
import lombok.NonNull;

public interface UserCommandService {

  /**
   * Register new user.
   *
   * @param registerUserDto the register user dto
   * @return Registered User Info
   */
  RegisterUserDto registerNewUser(@NonNull RegisterUserDto registerUserDto)
      throws ResourceExistException;
}
