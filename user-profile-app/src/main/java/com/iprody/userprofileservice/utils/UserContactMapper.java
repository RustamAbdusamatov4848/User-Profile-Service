package com.iprody.userprofileservice.utils;

import com.iprody.userprofileservice.dto.UserContactDto;
import com.iprody.userprofileservice.models.UserContact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserContactMapper {
    UserContactDto contactToContactDto(UserContact userContact);

    UserContact contactDtoToContact(UserContactDto userContactDto);
}
