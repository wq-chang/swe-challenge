package com.swe.challenge.service.model;

import com.swe.challenge.repository.entity.Invitation;
import com.swe.challenge.repository.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record SuggestRestaurantDto(
    @NotNull UUID invitationUuid,
    @NotBlank @Size(max = User.Attributes.USERNAME_MAX_LENGTH) String inviteeUsername,
    @NotBlank @Size(max = Invitation.Attributes.RESTAURANT_MAX_LENGTH) String restaurant) {}
