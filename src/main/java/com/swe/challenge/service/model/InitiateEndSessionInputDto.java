package com.swe.challenge.service.model;

import com.swe.challenge.repository.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record InitiateEndSessionInputDto(
    @NotBlank @Size(max = User.Attributes.USERNAME_MAX_LENGTH) String username) {}
