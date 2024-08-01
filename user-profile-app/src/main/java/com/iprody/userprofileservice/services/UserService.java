package com.iprody.userprofileservice.services;

import com.iprody.userprofileservice.dto.UserDto;
import com.iprody.userprofileservice.models.User;
import com.iprody.userprofileservice.repositories.UserRepository;
import com.iprody.userprofileservice.utils.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public Optional<UserDto> findUserById(Long id) {
        return userRepository
                .findById(id)
                .map(userMapper::userToUserDto);
    }

    @Transactional
    public UserDto save(UserDto userDTO) {
        User user = userMapper.userDtoToUser(userDTO);
        User savedUser = userRepository.save(user);
        log.info("Saved user with email: {}", savedUser.getEmail());
        return userMapper.userToUserDto(savedUser);
    }

    @Transactional
    public void updateUser(UserDto userDTO) {
        User user = userRepository
                .findById(userDTO.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("No user was found with ID: " + userDTO.getId()));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        userRepository.save(user);

        log.info("Updated user with ID: {}", user.getId());
    }
}
