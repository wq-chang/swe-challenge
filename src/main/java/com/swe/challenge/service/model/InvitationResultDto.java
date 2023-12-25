package com.swe.challenge.service.model;

import java.util.List;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record InvitationResultDto(
    List<String> newInvitedUsers, List<String> notFoundUsers, List<String> invitedUsers) {}
