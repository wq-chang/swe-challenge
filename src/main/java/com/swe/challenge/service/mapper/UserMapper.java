package com.swe.challenge.service.mapper;

import com.swe.challenge.repository.entity.User;
import com.swe.challenge.service.model.RegisterUserDto;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(imports = {UUID.class})
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(expression = "java(UUID.randomUUID())", target = "uuid")
  @Mapping(target = "sessions", ignore = true)
  @Mapping(target = "invitations", ignore = true)
  User registerUserDtoToUser(RegisterUserDto dto);
}
