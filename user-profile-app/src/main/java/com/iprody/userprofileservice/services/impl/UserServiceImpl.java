package com.iprody.userprofileservice.services.impl;

import com.iprody.userprofileservice.dto.UserDto;
import com.iprody.userprofileservice.models.User;
import com.iprody.userprofileservice.repositories.UserRepository;
import com.iprody.userprofileservice.services.UserService;
import com.iprody.userprofileservice.utils.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public Optional<UserDto> findUserById(Long userDd) {
        return userRepository
                .findById(userDd)
                .map(userMapper::userToUserDto);
    }

    @Transactional
    public UserDto createUser(UserDto userDTO) {
        User savedUser = userRepository
                .save(userMapper.userDtoToUser(userDTO));
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
}
