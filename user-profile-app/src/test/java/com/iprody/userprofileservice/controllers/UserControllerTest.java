package com.iprody.userprofileservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iprody.userprofileservice.dto.UserContactDto;
import com.iprody.userprofileservice.dto.UserDto;
import com.iprody.userprofileservice.services.UserContactService;
import com.iprody.userprofileservice.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserContactService userContactService;

    @Autowired
    private MockMvc mockMvc;

    private static final String VERSION = "v1";
    private static final String BASE_URL = "/" + VERSION + "/users";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void whenUserExists_thenUserShouldBeReturned() throws Exception {
        //given
        UserDto userDto = createUserDtoWithId();
        Long id = userDto.getId();

        //when
        when(userService.findUserById(id)).thenReturn(Optional.of(userDto));

        //then
        mockMvc.perform(get(BASE_URL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value("Pole"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("ps@test.com"))
                .andExpect(jsonPath("$.userContactId").value(userDto.getUserContactId()));

        verify(userService).findUserById(id);
    }

    @Test
    public void whenUserDoesNotExist_thenShouldReturn404NotFound() throws Exception {
        //given
        Long id = 1L;

        //when
        when(userService.findUserById(id)).thenReturn(Optional.empty());

        //then
        mockMvc.perform(get(BASE_URL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Failed entity search"))
                .andExpect(jsonPath("$.errors.cause").value("User with user ID: " + id + " not found"));

        verify(userService).findUserById(id);
    }

    @Test
    public void whenCreateUser_thenShouldReturnUserDto() throws Exception {
        //given
        UserDto userDto = createUserDtoWithNullId();
        UserDto createdUserDto = createUserDtoWithId();

        //when
        when(userService.createUser(userDto)).thenReturn(createdUserDto);

        //then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdUserDto.getId()))
                .andExpect(jsonPath("$.firstName").value("Pole"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("ps@test.com"))
                .andExpect(jsonPath("$.userContactId").value(createdUserDto.getUserContactId()));

        verify(userService).createUser(userDto);
    }

    @Test
    public void whenInvalidUser_thenShouldReturn400AndValidationErrors() throws Exception {
        //given
        UserDto invalidUserDto = new UserDto(
                1L,
                "A very long firstname that exceeds the maximum allowed length",
                "A very long lastname that exceeds the maximum allowed length",
                "invalid-email",
                1L);

        //when-then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.firstName").value("Firstname should not be more then 50 characters"))
                .andExpect(jsonPath("$.errors.lastName").value("Lastname should not be more then 50 characters"))
                .andExpect(jsonPath("$.errors.email").value("Invalid email format"));
    }

    @Test
    public void whenUpdateUser_thenShouldReturnNoContent() throws Exception {
        //given
        UserDto userDto = createUserDtoWithId();

        //when-then
        mockMvc.perform(put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNoContent());

        verify(userService).updateUser(userDto);
    }

    @Test
    public void whenDeleteUser_thenShouldReturnNoContent() throws Exception {
        //given
        Long id = 1L;

        //when-then
        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserById(id);
    }

    @Test
    public void whenGetUserContactExists_thenShouldReturnContact() throws Exception {
        //given
        UserContactDto userContactDto = createUserContactDto();
        Long contactId = userContactDto.getId();

        //when
        when(userContactService.findUserContactsById(contactId)).thenReturn(Optional.of(userContactDto));

        //then
        mockMvc.perform(get(BASE_URL + "/contacts/{id}", contactId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contactId))
                .andExpect(jsonPath("$.telegramId").value("@valid_id"))
                .andExpect(jsonPath("$.mobilePhone").value("+1234567890"));

        verify(userContactService).findUserContactsById(contactId);
    }

    @Test
    public void whenGetUserContactNotFound_thenShouldReturn404NotFound() throws Exception {
        //given
        Long contactId = 1L;

        //when
        when(userContactService.findUserContactsById(contactId)).thenReturn(Optional.empty());

        //then
        mockMvc.perform(get(BASE_URL + "/contacts/{id}", contactId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Failed entity search"))
                .andExpect(jsonPath("$.errors.cause")
                        .value("User contact with user contact ID: " + contactId + " not found"));

        verify(userContactService).findUserContactsById(contactId);
    }

    @Test
    public void whenGetUserContactByUserIdExists_thenShouldReturnContact() throws Exception {
        //given
        UserContactDto userContactDto = createUserContactDto();
        UserDto userDto = createUserDtoWithId();

        userDto.setUserContactId(userContactDto.getId());
        Long userDtoId = userDto.getId();

        //when
        when(userContactService.findUserContactByUserId(userDtoId))
                .thenReturn(Optional.of(userContactDto));

        //then
        mockMvc.perform(get(BASE_URL + "/{id}/contacts", userDtoId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userContactDto.getId()))
                .andExpect(jsonPath("$.telegramId").value("@valid_id"))
                .andExpect(jsonPath("$.mobilePhone").value("+1234567890"));

        verify(userContactService).findUserContactByUserId(userDtoId);
    }

    @Test
    public void whenGetUserContactByUserIdDoesNotExist_thenShouldReturn404NotFound() throws Exception {
        //given
        UserDto userDto = createUserDtoWithId();
        Long userDtoId = userDto.getId();

        //when
        when(userContactService.findUserContactByUserId(userDtoId))
                .thenReturn(Optional.empty());

        //then
        mockMvc.perform(get(BASE_URL + "/{id}/contacts", userDtoId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Failed entity search"))
                .andExpect(jsonPath("$.errors.cause")
                        .value("User contact with user ID: " + userDtoId + " not found"));

        verify(userContactService).findUserContactByUserId(userDtoId);
    }

    @Test
    public void whenUpdateUserContactByUserId_thenReturnNoContent() throws Exception {
        //given
        Long userDtoId = 1L;
        UserContactDto userContactDto = createUserContactDto();

        //when-then
        mockMvc.perform(put(BASE_URL + "/{id}/contacts", userDtoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userContactDto)))
                .andExpect(status().isNoContent());

        verify(userContactService).updateContacts(userContactDto, 1L);
    }

    @Test
    public void whenUpdateInvalidUserContact_thenReturnBadRequest() throws Exception {
        //given
        Long userDtoId = 1L;
        UserContactDto userContactDto = new UserContactDto();
        userContactDto.setTelegramId("invalidTelegramId");
        userContactDto.setMobilePhone("123");

        //when-then
        mockMvc.perform(put(BASE_URL + "/{id}/contacts", userDtoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userContactDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.telegramId").value("Invalid telegram ID format"))
                .andExpect(jsonPath("$.errors.mobilePhone").value("Invalid phone format"));
    }

    private static UserDto createUserDtoWithId() {
        return UserDto.builder()
                .id(1L)
                .firstName("Pole")
                .lastName("Smith")
                .email("ps@test.com")
                .userContactId(1L)
                .build();
    }

    private static UserDto createUserDtoWithNullId() {
        return UserDto.builder()
                .firstName("Pole")
                .lastName("Smith")
                .email("ps@test.com")
                .userContactId(1L)
                .build();
    }

    private static UserContactDto createUserContactDto() {
        return UserContactDto.builder()
                .id(1L)
                .telegramId("@valid_id")
                .mobilePhone("+1234567890")
                .build();
    }
}
