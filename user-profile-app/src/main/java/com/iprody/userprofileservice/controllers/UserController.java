package com.iprody.userprofileservice.controllers;

import com.iprody.userprofileservice.dto.UserContactDto;
import com.iprody.userprofileservice.dto.UserDto;
import com.iprody.userprofileservice.services.UserContactService;
import com.iprody.userprofileservice.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserContactService userContactService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId) {
        return userService.findUserById(userId)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User with ID: " + userId + " not found"));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        UserDto createdUserDto = userService.createUser(userDto);
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDto userDto,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        userService.updateUser(userDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/contacts/{id}")
    public ResponseEntity<UserContactDto> getUserContactByContactId(@PathVariable("id") Long userContactId) {
        return userContactService.findUserContactsById(userContactId)
                .map(contact -> new ResponseEntity<>(contact, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User contact with ID: " + userContactId + " not found"));
    }

    @GetMapping("/{id}/contacts")
    public ResponseEntity<UserContactDto> getUserContactByUserId(@PathVariable("id") Long userId) {
        return userContactService.findUserContactByUserId(userId)
                .map(contact -> new ResponseEntity<>(contact, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User contact with user ID: " + userId + " not found"));
    }

    @PutMapping("/{id}/contacts")
    public ResponseEntity<?> updateUserContactByUserId(
            @PathVariable("id") Long userId,
            @Valid @RequestBody UserContactDto userContactDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        userContactService.updateContacts(userContactDto, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<Object> handleValidationErrors(BindingResult bindingResult) {
        List<String> errors = bindingResult
                .getAllErrors()
                .stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }
}
