package com.iprody.userprofileservice.controllers;

import com.iprody.userprofileservice.dto.UserContactDto;
import com.iprody.userprofileservice.dto.UserDto;
import com.iprody.userprofileservice.exceptions.ResourceNotFoundException;
import com.iprody.userprofileservice.models.Role;
import com.iprody.userprofileservice.services.UserContactService;
import com.iprody.userprofileservice.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.V1UserApi;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements V1UserApi {

    private final UserService userService;
    private final UserContactService userContactService;

    @Override
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId) {
        return userService.findUserById(userId)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }

    @Override
    public ResponseEntity<List<UserDto>> getAllUsers(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<UserDto> userDtoList = userService.getAllUsers(pageable).getContent();
        return ResponseEntity.ok(userDtoList);
    }

    @Override
    public ResponseEntity<List<UserDto>> getUsersByRole(Role userRole) {
        return ResponseEntity.ok(userService.getUsersByRole(userRole));
    }

    @Override
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUserDto = userService.createUser(userDto);
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> updateUser(@Valid @RequestBody UserDto userDto) {
        if (!userService.isExistUser(userDto.getId())) {
            throw new ResourceNotFoundException("User", userDto.getId());
        }
        userService.updateUser(userDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long userId) {
        if (!userService.isExistUser(userId)) {
            throw new ResourceNotFoundException("User", userId);
        }
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserContactDto> getUserContactByContactId(@PathVariable("id") Long userContactId) {
        return userContactService.findUserContactsById(userContactId)
                .map(contact -> new ResponseEntity<>(contact, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("User contact", userContactId));
    }

    @Override
    public ResponseEntity<UserContactDto> getUserContactByUserId(@PathVariable("id") Long userId) {
        return userContactService.findUserContactByUserId(userId)
                .map(contact -> new ResponseEntity<>(contact, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("User contact", userId));
    }

    @Override
    public ResponseEntity<UserContactDto> updateUserContactByUserId(
            @PathVariable("id") Long userId,
            @Valid @RequestBody UserContactDto userContactDto) {

        userContactService.updateContacts(userContactDto, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
