package com.swe.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.swe.challenge.exception.ResourceExistException;
import com.swe.challenge.repository.UserRepository;
import com.swe.challenge.service.model.RegisterUserDto;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserCommandServiceImplTest {

  @Autowired private UserCommandService userCommandService;

  @Autowired private UserRepository userRepository;

  @AfterEach
  void clean() {
    userRepository.deleteAll();
  }

  @Test
  void registerNewUser_Valid_Success() throws ResourceExistException {
    val dto = new RegisterUserDto("a", "a");
    val result = userCommandService.registerNewUser(dto);

    assertEquals(dto, result);
  }

  @Test
  void registerNewUser_UserExist_Failed() throws ResourceExistException {
    val dto = new RegisterUserDto("a", "a");
    userCommandService.registerNewUser(dto);

    assertThrows(ResourceExistException.class, () -> userCommandService.registerNewUser(dto));
  }
}
