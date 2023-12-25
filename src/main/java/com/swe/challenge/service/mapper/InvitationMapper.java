package com.swe.challenge.service.mapper;

import com.swe.challenge.repository.entity.Invitation;
import com.swe.challenge.service.model.InvitationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InvitationMapper {

  InvitationMapper INSTANCE = Mappers.getMapper(InvitationMapper.class);

  @Mapping(source = "invitation.session.uuid", target = "sessionUuid")
  @Mapping(source = "invitation.uuid", target = "invitationUuid")
  @Mapping(source = "invitation.session.owner.username", target = "ownerUsername")
  @Mapping(source = "invitation.session.active", target = "sessionActive")
  @Mapping(source = "invitation.session.pickedRestaurant", target = "pickedRestaurant")
  InvitationDto invitationToInvitationDto(Invitation invitation);
}
