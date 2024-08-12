package com.iprody.userprofileservice.services;

import com.iprody.userprofileservice.dto.UserContactDto;

import java.util.Optional;

public interface UserContactService {
    Optional<UserContactDto> findUserContactsById(Long userContactId);

    Optional<UserContactDto> findUserContactByUserId(Long userId);

    void updateContacts(UserContactDto userContactDto, Long userContactId);
}
