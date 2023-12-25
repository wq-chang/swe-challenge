package com.swe.challenge.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swe.challenge.service.SessionCommandService;
import com.swe.challenge.service.model.InitiateEndSessionInputDto;
import com.swe.challenge.service.model.InviteInputDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SessionCommandController.class)
class SessionCommandControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private SessionCommandService sessionCommandService;

  @Test
  void initiateNewSession_ValidInput_Success() throws Exception {
    val username = "username";
    val dto = new InitiateEndSessionInputDto(username);
    ObjectMapper objectMapper = new ObjectMapper();
    val inputString = objectMapper.writeValueAsString(dto);
    mockMvc
        .perform(
            post("/sessions/initiate").contentType(MediaType.APPLICATION_JSON).content(inputString))
        .andExpect(status().isOk());
    verify(sessionCommandService, times(1)).initiateSession(username);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void initiateNewSession_InvalidInput_Failed(String username) throws Exception {
    val dto = new InitiateEndSessionInputDto(username);
    ObjectMapper objectMapper = new ObjectMapper();
    val inputString = objectMapper.writeValueAsString(dto);
    mockMvc
        .perform(
            post("/sessions/initiate").contentType(MediaType.APPLICATION_JSON).content(inputString))
        .andExpect(status().isBadRequest());
  }

  @Test
  void inviteUsers_ValidInput_Success() throws Exception {
    val ownerUsername = "username";
    val inviteeUsernames = List.of("a", "b");
    val dto = new InviteInputDto(ownerUsername, inviteeUsernames);
    ObjectMapper objectMapper = new ObjectMapper();
    val inputString = objectMapper.writeValueAsString(dto);
    mockMvc
        .perform(
            post("/sessions/invite-users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputString))
        .andExpect(status().isOk());
    verify(sessionCommandService, times(1)).inviteUsers(ownerUsername, inviteeUsernames);
  }

  @ParameterizedTest
  @MethodSource("provideInviteUsersInvalidInputs")
  void inviteUsers_InvalidInput_Failed(String ownerUsername, List<String> inviteeUsernames)
      throws Exception {
    val dto = new InviteInputDto(ownerUsername, inviteeUsernames);
    ObjectMapper objectMapper = new ObjectMapper();
    val inputString = objectMapper.writeValueAsString(dto);
    mockMvc
        .perform(
            post("/sessions/invite-users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputString))
        .andExpect(status().isBadRequest());
  }

  private static Stream<Arguments> provideInviteUsersInvalidInputs() {
    val list = List.of("a");
    return Stream.of(
        Arguments.of(null, list),
        Arguments.of("", list),
        Arguments.of(" ", list),
        Arguments.of("a", null),
        Arguments.of("a", new ArrayList<>()),
        Arguments.of("a", List.of("")));
  }

  @Test
  void endSession_ValidInput_Success() throws Exception {
    val ownerUsername = "username";
    val dto = new InitiateEndSessionInputDto(ownerUsername);
    ObjectMapper objectMapper = new ObjectMapper();
    val inputString = objectMapper.writeValueAsString(dto);
    mockMvc
        .perform(post("/sessions/end").contentType(MediaType.APPLICATION_JSON).content(inputString))
        .andExpect(status().isOk());
    verify(sessionCommandService, times(1)).endSession(ownerUsername);
  }
}
