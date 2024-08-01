package com.iprody.userprofileservice.services;

import com.iprody.userprofileservice.dto.UserDto;
import com.iprody.userprofileservice.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIntegrationTest {
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceIntegrationTest(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Test
    public void saveUser_NewUser_UserIsSaved() {
        //Given
        UserDto userDTO = new UserDto(
                null,
                "Firstname",
                "Lastname",
                "test@example.com");

        //When
        UserDto savedUser = userService.save(userDTO);


        //Then
        assertNotNull(savedUser.getId());
        assertEquals("Firstname", savedUser.getFirstName());
        assertEquals("Lastname", savedUser.getLastName());
        assertEquals("test@example.com", savedUser.getEmail());
    }

    @Test
    public void updateUser_ExistingUser_UserIsUpdated() {
        //Given
        UserDto userDTO = new UserDto(
                null,
                "Firstname",
                "Lastname",
                "test@example.com");
        UserDto savedUser = userService.save(userDTO);

        //When
        savedUser.setFirstName("Pole");
        savedUser.setLastName("Smith");
        savedUser.setEmail("pole.smith@example.com");
        userService.updateUser(savedUser);

        //Then
        UserDto updatedUser = userService
                .findUserById(savedUser.getId())
                .orElse(null);
        assertNotNull(updatedUser);
        assertEquals("Pole", updatedUser.getFirstName());
        assertEquals("Smith", updatedUser.getLastName());
        assertEquals("pole.smith@example.com", updatedUser.getEmail());
    }

    @Test
    public void findUserById_ExistingUserId_UserIsFound() {
        //Given
        UserDto userDTO = new UserDto(
                null,
                "Firstname",
                "Lastname",
                "test.@example.com");
        UserDto savedUser = userService.save(userDTO);

        //When
        Optional<UserDto> foundUserOptional = userService.findUserById(savedUser.getId());

        //Then
        assertTrue(foundUserOptional.isPresent());
        UserDto foundUser = foundUserOptional.get();
        assertEquals("Firstname", foundUser.getFirstName());
        assertEquals("Lastname", foundUser.getLastName());
        assertEquals("test.@example.com", foundUser.getEmail());
    }

    @Test
    public void findUserById_NonExistingUserId_UserIsNotFound() {
        //Given
        Long nonExistingUserId = 999L;

        //When
        Optional<UserDto> foundUserOptional = userService.findUserById(nonExistingUserId);

        //Then
        assertFalse(foundUserOptional.isPresent(),
                "Expected no user to be found for ID: " + nonExistingUserId);
    }
}
