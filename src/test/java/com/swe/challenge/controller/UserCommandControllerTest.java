package com.swe.challenge.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swe.challenge.repository.entity.User;
import com.swe.challenge.service.UserCommandService;
import com.swe.challenge.service.model.RegisterUserDto;
import java.util.stream.Stream;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserCommandController.class)
class UserCommandControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private UserCommandService userCommandService;

  @Test
  void registerNewUser_ValidInput_Success() throws Exception {
    val dto = new RegisterUserDto("username", "display name");
    ObjectMapper objectMapper = new ObjectMapper();
    val dtoString = objectMapper.writeValueAsString(dto);
    mockMvc
        .perform(post("/users/register").contentType(MediaType.APPLICATION_JSON).content(dtoString))
        .andExpect(status().isOk());
    verify(userCommandService, times(1)).registerNewUser(dto);
  }

  @ParameterizedTest
  @MethodSource("provideRegisterNewUserInvalidInputs")
  void registerNewUser_InvalidInput_Failed(RegisterUserDto dto) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    val dtoString = objectMapper.writeValueAsString(dto);
    mockMvc
        .perform(post("/users/register").contentType(MediaType.APPLICATION_JSON).content(dtoString))
        .andExpect(status().isBadRequest());
  }

  private static Stream<Arguments> provideRegisterNewUserInvalidInputs() {
    val usernameLongString = StringUtils.repeat("a", User.Attributes.USERNAME_MAX_LENGTH + 1);
    val displayNameLongString =
        StringUtils.repeat("a", User.Attributes.DISPLAY_NAME_MAX_LENGTH + 1);
    return Stream.of(
        Arguments.of(new RegisterUserDto(null, "a")),
        Arguments.of(new RegisterUserDto("a", null)),
        Arguments.of(new RegisterUserDto("", "a")),
        Arguments.of(new RegisterUserDto("a", "")),
        Arguments.of(new RegisterUserDto(usernameLongString, "a")),
        Arguments.of(new RegisterUserDto("a", displayNameLongString)));
  }
}
