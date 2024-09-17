package com.iprody.userprofileservice.services;

import com.iprody.userprofileservice.dto.UserDto;
import com.iprody.userprofileservice.models.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDto> findUserById(Long userId);

    UserDto createUser(UserDto userDTO);

    List<UserDto> getUsersByRole(Role userRole);

    Page<UserDto> getAllUsers(Pageable pageable);

    void updateUser(UserDto userDTO);

    void deleteUserById(Long id);

    boolean isExistUser(Long id);
}
