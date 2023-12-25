package com.swe.challenge.service.model;

import com.swe.challenge.repository.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record ListInvitationInputDto(
    @NotBlank @Size(max = User.Attributes.USERNAME_MAX_LENGTH) String inviteeUsername,
    @NotEmpty List<@NotNull Boolean> accepted,
    @NotEmpty List<@NotNull Boolean> sessionActive) {}
