package com.iprody.userprofileservice.services.impl;

import com.iprody.userprofileservice.dto.UserDto;
import com.iprody.userprofileservice.models.Role;
import com.iprody.userprofileservice.models.User;
import com.iprody.userprofileservice.models.UserContact;
import com.iprody.userprofileservice.repositories.UserContactRepository;
import com.iprody.userprofileservice.repositories.UserRepository;
import com.iprody.userprofileservice.services.UserService;
import com.iprody.userprofileservice.utils.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserContactRepository userContactRepository;


    public Optional<UserDto> findUserById(Long userId) {
        return userRepository
                .findById(userId)
                .map(userMapper::userToUserDto);
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository
                .findAll(pageable)
                .map(userMapper::userToUserDto);
    }

    @Override
    public List<UserDto> getUsersByRole(Role userRole) {
        return userRepository
                .findAllByUserRole(userRole)
                .stream()
                .map(userMapper::userToUserDto)
                .toList();
    }

    @Transactional
    public UserDto createUser(UserDto userDTO) {
        User user = userMapper.userDtoToUser(userDTO);

        if (user.getUserContact() != null && user.getUserContact().getId() != null) {
            UserContact existingContact = userContactRepository
                    .getReferenceById(user.getUserContact().getId());
            user.setUserContact(existingContact);
        }

        User savedUser = userRepository.save(user);
        log.info("Saved user with ID: {}", savedUser.getId());

        return userMapper.userToUserDto(savedUser);
    }

    @Transactional
    public void updateUser(UserDto userDTO) {
        userRepository.findById(userDTO.getId())
                .map(user -> userMapper.updateUserFromDto(userDTO, user))
                .map(userRepository::save);
        log.info("Update user with ID: {}", userDTO.getId());
    }

    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
        log.info("Delete user with ID: {}", id);
    }

    public boolean isExistUser(Long userId) {
        return userRepository.existsById(userId);
    }
}
