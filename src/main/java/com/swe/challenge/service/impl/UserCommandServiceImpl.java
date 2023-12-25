package com.swe.challenge.service.impl;

import com.swe.challenge.exception.ResourceExistException;
import com.swe.challenge.repository.UserRepository;
import com.swe.challenge.service.UserCommandService;
import com.swe.challenge.service.mapper.UserMapper;
import com.swe.challenge.service.model.RegisterUserDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

  private final UserRepository userRepository;

  @Override
  public RegisterUserDto registerNewUser(@NonNull RegisterUserDto registerUserDto)
      throws ResourceExistException {
    boolean usernameExist = userRepository.countByUsername(registerUserDto.username()) > 0;
    if (usernameExist) {
      throw new ResourceExistException("Username existed");
    }

    val user = UserMapper.INSTANCE.registerUserDtoToUser(registerUserDto);

    userRepository.save(user);

    return registerUserDto;
  }
}
