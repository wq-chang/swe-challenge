package com.swe.challenge.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swe.challenge.repository.entity.User;
import com.swe.challenge.service.InvitationQueryService;
import com.swe.challenge.service.model.ListInvitationInputDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

@WebMvcTest(InvitationQueryController.class)
class InvitationQueryControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private InvitationQueryService invitationQueryService;

  @Test
  void listInvitations_ValidInput_Success() throws Exception {
    val username = "username";
    val accepted = List.of(true);
    val active = List.of(true);
    val dto = new ListInvitationInputDto(username, accepted, active);
    ObjectMapper objectMapper = new ObjectMapper();
    val dtoString = objectMapper.writeValueAsString(dto);
    mockMvc
        .perform(
            post("/invitations/search").contentType(MediaType.APPLICATION_JSON).content(dtoString))
        .andExpect(status().isOk());
    verify(invitationQueryService, times(1)).listInvitation(username, accepted, active);
  }

  @ParameterizedTest
  @MethodSource("provideListInvitationsInvalidInputs")
  void listInvitations_InvalidInput_Failed(ListInvitationInputDto dto) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    val dtoString = objectMapper.writeValueAsString(dto);
    mockMvc
        .perform(
            post("/invitations/search").contentType(MediaType.APPLICATION_JSON).content(dtoString))
        .andExpect(status().isBadRequest());
  }

  private static Stream<Arguments> provideListInvitationsInvalidInputs() {
    val usernameLongString = StringUtils.repeat("a", User.Attributes.USERNAME_MAX_LENGTH + 1);
    val list = List.of(true);
    return Stream.of(
        Arguments.of(new ListInvitationInputDto(null, list, list)),
        Arguments.of(new ListInvitationInputDto("", list, list)),
        Arguments.of(new ListInvitationInputDto(" ", list, list)),
        Arguments.of(new ListInvitationInputDto(usernameLongString, list, list)),
        Arguments.of(new ListInvitationInputDto("a", null, list)),
        Arguments.of(new ListInvitationInputDto("a", new ArrayList<>(), list)),
        Arguments.of(new ListInvitationInputDto("a", list, null)),
        Arguments.of(new ListInvitationInputDto("a", list, new ArrayList<>())));
  }

  @Test
  void listRestaurants_ValidInput_Success() throws Exception {
    val username = "username";
    val uuid = UUID.randomUUID();
    mockMvc.perform(get(STR."/invitations/\{username}/\{uuid}/restaurants")).andExpect(status().isOk());
    verify(invitationQueryService, times(1)).listRestaurants(username, uuid);
  }

  @ParameterizedTest
  @MethodSource("provideListRestaurantsInvalidInputs")
  void listRestaurants_InvalidInput_Failed(String username, UUID uuid) throws Exception {
    mockMvc.perform(get(STR."/invitations/\{username}/\{uuid}/restaurants")).andExpect(status().isBadRequest());
  }

  private static Stream<Arguments> provideListRestaurantsInvalidInputs() {
    val usernameLongString = StringUtils.repeat("a", User.Attributes.USERNAME_MAX_LENGTH + 1);
    val uuid = UUID.randomUUID();
    return Stream.of(
        Arguments.of(" ", uuid),
        Arguments.of(usernameLongString, uuid),
        Arguments.of("a", null));
  }
}
