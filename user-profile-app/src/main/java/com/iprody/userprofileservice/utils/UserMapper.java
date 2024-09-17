package com.iprody.userprofileservice.utils;

import com.iprody.userprofileservice.dto.UserDto;
import com.iprody.userprofileservice.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserContactMapper.class})
public interface UserMapper {

    @Mapping(source = "userContact.id", target = "userContactId")
    @Mapping(source = "userRole", target = "userRole")
    UserDto userToUserDto(User user);

    @Mapping(source = "userContactId", target = "userContact.id")
    @Mapping(source = "userRole", target = "userRole")
    User userDtoToUser(UserDto userDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userContact", ignore = true)
    User updateUserFromDto(UserDto userDto, @MappingTarget User user);
}
