package com.swe.challenge.service.model;

import java.util.UUID;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record SessionDto(
    UUID uuid, String ownerDisplayName, boolean active, String pickedRestaurant) {}
