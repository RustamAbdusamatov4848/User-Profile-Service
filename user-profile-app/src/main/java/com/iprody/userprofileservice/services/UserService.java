package com.iprody.userprofileservice.services;

import com.iprody.userprofileservice.dto.UserDto;

import java.util.Optional;

public interface UserService {
    Optional<UserDto> findUserById(Long userId);

    UserDto createUser(UserDto userDTO);

    void updateUser(UserDto userDTO);

    void deleteUserById(Long id);

    boolean isExistUser(Long id);
}
