package com.iprody.userprofileservice.services.impl;

import com.iprody.userprofileservice.dto.UserContactDto;
import com.iprody.userprofileservice.models.User;
import com.iprody.userprofileservice.repositories.UserContactRepository;
import com.iprody.userprofileservice.repositories.UserRepository;
import com.iprody.userprofileservice.services.UserContactService;
import com.iprody.userprofileservice.utils.UserContactMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserContactServiceImpl implements UserContactService {
    private final UserRepository userRepository;
    private final UserContactRepository userContactRepository;
    private final UserContactMapper userContactMapper;

    @Override
    public Optional<UserContactDto> findUserContactsById(Long userContactId) {
        return userContactRepository
                .findById(userContactId)
                .map(userContactMapper::contactToContactDto);
    }

    @Override
    public Optional<UserContactDto> findUserContactByUserId(Long userId) {
        return userRepository
                .findById(userId)
                .map(User::getUserContact)
                .map(userContactMapper::contactToContactDto);
    }

    @Override
    @Transactional
    public void updateContacts(UserContactDto userContactDto, Long userId) {
        userRepository.findById(userId)
                .map(User::getUserContact)
                .map(userContact -> userContactMapper.updateUserContactFromDto(userContactDto, userContact))
                .map(userContactRepository::save);

        log.info("User contacts updated for user with ID: {}", userId);
    }
}
