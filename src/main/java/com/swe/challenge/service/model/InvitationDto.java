package com.swe.challenge.service.model;

import java.util.UUID;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record InvitationDto(
    UUID sessionUuid,
    UUID invitationUuid,
    String ownerUsername,
    boolean sessionActive,
    boolean accepted,
    String restaurant,
    String pickedRestaurant) {}
