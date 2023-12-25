package com.swe.challenge.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swe.challenge.repository.entity.Invitation;
import com.swe.challenge.repository.entity.User;
import com.swe.challenge.service.InvitationCommandService;
import com.swe.challenge.service.model.AcceptInvitationDto;
import com.swe.challenge.service.model.SuggestRestaurantDto;
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

@WebMvcTest(InvitationCommandController.class)
class InvitationCommandControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private InvitationCommandService invitationCommandService;

  @Test
  void acceptInvitation_ValidInput_Success() throws Exception {
    val username = "username";
    val uuid = UUID.randomUUID();
    val dto = new AcceptInvitationDto(uuid, username);
    ObjectMapper objectMapper = new ObjectMapper();
    val dtoString = objectMapper.writeValueAsString(dto);
    mockMvc
        .perform(
            post("/invitations/accept").contentType(MediaType.APPLICATION_JSON).content(dtoString))
        .andExpect(status().isOk());
    verify(invitationCommandService, times(1)).acceptInvitation(username, uuid);
  }

  @ParameterizedTest
  @MethodSource("provideAcceptInvitationInvalidInputs")
  void acceptInvitation_InvalidInput_Failed(AcceptInvitationDto dto) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    val dtoString = objectMapper.writeValueAsString(dto);
    mockMvc
        .perform(
            post("/invitations/accept").contentType(MediaType.APPLICATION_JSON).content(dtoString))
        .andExpect(status().isBadRequest());
  }

  private static Stream<Arguments> provideAcceptInvitationInvalidInputs() {
    val usernameLongString = StringUtils.repeat("a", User.Attributes.USERNAME_MAX_LENGTH + 1);
    val uuid = UUID.randomUUID();
    return Stream.of(
        Arguments.of(new AcceptInvitationDto(null, "a")),
        Arguments.of(new AcceptInvitationDto(uuid, "")),
        Arguments.of(new AcceptInvitationDto(uuid, " ")),
        Arguments.of(new AcceptInvitationDto(uuid, usernameLongString)));
  }

  @Test
  void suggestRestaurant_ValidInput_Success() throws Exception {
    val username = "username";
    val uuid = UUID.randomUUID();
    val restaurant = "restaurant";
    val dto = new SuggestRestaurantDto(uuid, username, restaurant);
    ObjectMapper objectMapper = new ObjectMapper();
    val dtoString = objectMapper.writeValueAsString(dto);
    mockMvc
        .perform(
            post("/invitations/suggest-restaurant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtoString))
        .andExpect(status().isOk());
    verify(invitationCommandService, times(1)).suggestRestaurant(username, uuid, restaurant);
  }

  @ParameterizedTest
  @MethodSource("provideSuggestRestaurantInvalidInputs")
  void suggestRestaurant_InvalidInput_Failed(SuggestRestaurantDto dto) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    val dtoString = objectMapper.writeValueAsString(dto);
    mockMvc
        .perform(
            post("/invitations/suggest-restaurant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtoString))
        .andExpect(status().isBadRequest());
  }

  private static Stream<Arguments> provideSuggestRestaurantInvalidInputs() {
    val usernameLongString = StringUtils.repeat("a", User.Attributes.USERNAME_MAX_LENGTH + 1);
    val restaurantLongString =
        StringUtils.repeat("a", Invitation.Attributes.RESTAURANT_MAX_LENGTH + 1);
    val uuid = UUID.randomUUID();
    return Stream.of(
        Arguments.of(new SuggestRestaurantDto(null, "a", "a")),
        Arguments.of(new SuggestRestaurantDto(uuid, null, "a")),
        Arguments.of(new SuggestRestaurantDto(uuid, "", "a")),
        Arguments.of(new SuggestRestaurantDto(uuid, " ", "a")),
        Arguments.of(new SuggestRestaurantDto(uuid, usernameLongString, "a")),
        Arguments.of(new SuggestRestaurantDto(uuid, "a", null)),
        Arguments.of(new SuggestRestaurantDto(uuid, "a", "")),
        Arguments.of(new SuggestRestaurantDto(uuid, "a", " ")),
        Arguments.of(new SuggestRestaurantDto(uuid, "a", restaurantLongString)));
  }
}
