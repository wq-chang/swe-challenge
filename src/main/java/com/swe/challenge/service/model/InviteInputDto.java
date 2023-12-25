package com.swe.challenge.service.model;

import com.swe.challenge.repository.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record InviteInputDto(
    @NotBlank @Size(max = User.Attributes.USERNAME_MAX_LENGTH) String ownerUsername,
    @NotEmpty List<@NotBlank String> inviteeUsernames) {}
