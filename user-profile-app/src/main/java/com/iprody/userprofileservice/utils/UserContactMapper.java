package com.iprody.userprofileservice.utils;

import com.iprody.userprofileservice.dto.UserContactDto;
import com.iprody.userprofileservice.models.UserContact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserContactMapper {
    UserContactDto contactToContactDto(UserContact userContact);

    UserContact contactDtoToContact(UserContactDto userContactDto);

    @Mapping(target = "id", ignore = true)
    UserContact updateUserContactFromDto(UserContactDto userContactDto, @MappingTarget UserContact userContact);
}
