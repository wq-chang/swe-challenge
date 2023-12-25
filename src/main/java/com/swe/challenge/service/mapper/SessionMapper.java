package com.swe.challenge.service.mapper;

import com.swe.challenge.repository.entity.Session;
import com.swe.challenge.service.model.SessionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SessionMapper {

  SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

  @Mapping(source = "session.owner.displayName", target = "ownerDisplayName")
  SessionDto sessionToSessionDto(Session session);
}
