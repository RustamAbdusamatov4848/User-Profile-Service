package com.iprody.userprofileservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iprody.userprofileservice.dto.UserContactDto;
import com.iprody.userprofileservice.dto.UserDto;
import com.iprody.userprofileservice.models.Role;
import com.iprody.userprofileservice.services.UserContactService;
import com.iprody.userprofileservice.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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
        // given
        UserDto userDto = createUserDtoWithId();
        Long id = userDto.getId();

        // when
        when(userService.findUserById(id)).thenReturn(Optional.of(userDto));

        // then
        mockMvc.perform(get(BASE_URL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value("Pole"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("ps@test.com"))
                .andExpect(jsonPath("$.userContactId").value(userDto.getUserContactId()))
                .andExpect(jsonPath("$.userRole").value(String.valueOf(userDto.getUserRole())));

        verify(userService).findUserById(id);
    }

    @Test
    public void whenUserDoesNotExist_thenShouldReturn404NotFound() throws Exception {
        // given
        Long id = 1L;

        // when
        when(userService.findUserById(id)).thenReturn(Optional.empty());

        // then
        mockMvc.perform(get(BASE_URL + "/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Failed entity search"))
                .andExpect(jsonPath("$.errors.cause").value("User with ID: " + id + ", not found"));

        verify(userService).findUserById(id);
    }

    @Test
    public void whenGetAllUsers_thenAllUsersShouldBeReturned() throws Exception {
        // given
        List<UserDto> userDtoList = createListOfUserDto(8, Role.MANAGER);
        Page<UserDto> pageResult = new PageImpl<>(userDtoList);

        Pageable pageable = PageRequest.of(0, 2);

        when(userService.getAllUsers(pageable)).thenReturn(pageResult);

        // when-then
        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(userDtoList.size()));

        verify(userService).getAllUsers(pageable);
    }

    @Test
    void whenGetAllUsers_thenShouldReturnEmptyListWhenNoUsers() throws Exception {
        // given
        List<UserDto> emptyUserList = List.of();
        Page<UserDto> pageResult = new PageImpl<>(emptyUserList);

        Pageable pageable = PageRequest.of(0, 2);

        when(userService.getAllUsers(pageable)).thenReturn(pageResult);

        // when-then
        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(userService).getAllUsers(pageable);
    }

    @Test
    void whenGetUsersByRole_thenShouldReturnListOfUsers() throws Exception {
        // given
        Role role = Role.MANAGER;
        List<UserDto> userList = createListOfUserDto(8, role);

        when(userService.getUsersByRole(role)).thenReturn(userList);

        // when-then
        mockMvc.perform(get(BASE_URL + "/role")
                        .param("role", String.valueOf(role))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(userList.size()));

        verify(userService).getUsersByRole(role);
    }

    @Test
    void whenGetUsersByRole_thenShouldReturnEmptyListIfNoUsersWithGivenRole() throws Exception {
        // given
        Role specifiedRole = Role.SYSTEM_ADMIN;
        List<UserDto> userListWithoutSpecifiedRole = createListOfUserDto(8, Role.MANAGER);

        when(userService.getUsersByRole(specifiedRole)).thenReturn(List.of());

        // when-then
        mockMvc.perform(get(BASE_URL + "/role")
                        .param("role", String.valueOf(specifiedRole))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(userService).getUsersByRole(specifiedRole);
    }

    @Test
    public void whenCreateUser_thenShouldReturnUserDto() throws Exception {
        // given
        UserDto userDto = createUserDtoWithNullId();
        UserDto createdUserDto = createUserDtoWithId();

        // when
        when(userService.createUser(userDto)).thenReturn(createdUserDto);

        // then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdUserDto.getId()))
                .andExpect(jsonPath("$.firstName").value("Pole"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("ps@test.com"))
                .andExpect(jsonPath("$.userContactId").value(createdUserDto.getUserContactId()))
                .andExpect(jsonPath("$.userRole").value(String.valueOf(createdUserDto.getUserRole())));

        verify(userService).createUser(userDto);
    }

    @Test
    public void whenInvalidUser_thenShouldReturn400AndValidationErrors() throws Exception {
        // given
        UserDto invalidUserDto = new UserDto(
                1L,
                "A very long firstname that exceeds the maximum allowed length",
                "A very long lastname that exceeds the maximum allowed length",
                "invalid-email",
                1L,
                Role.MANAGER);

        // when-then
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
        // given
        UserDto userDto = createUserDtoWithId();
        when(userService.isExistUser(userDto.getId())).thenReturn(true);

        // when-then
        mockMvc.perform(put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNoContent());

        verify(userService).updateUser(userDto);
    }

    @Test
    public void whenUpdateNonExistingUser_thenShouldReturnNotFound() throws Exception {
        // given
        Long nonExistingId = 1000000L;
        UserDto userDto = createUserDtoWithId();
        userDto.setId(nonExistingId);

        // when-then
        mockMvc.perform(put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Failed entity search"))
                .andExpect(jsonPath("$.errors.cause")
                        .value("User with ID: " + nonExistingId + ", not found"));
    }

    @Test
    public void whenDeleteUser_thenShouldReturnNoContent() throws Exception {
        // given
        Long id = 1L;
        when(userService.isExistUser(id)).thenReturn(true);

        // when-then
        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserById(id);
    }

    @Test
    public void whenDeleteNonExistingUser_thenShouldReturnNotFound() throws Exception {
        // given
        Long nonExistingId = 1000000L;

        // when-then
        mockMvc.perform(delete(BASE_URL + "/{id}", nonExistingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Failed entity search"))
                .andExpect(jsonPath("$.errors.cause")
                        .value("User with ID: " + nonExistingId + ", not found"));
    }

    @Test
    public void whenGetUserContactExists_thenShouldReturnContact() throws Exception {
        // given
        UserContactDto userContactDto = createUserContactDto();
        Long contactId = userContactDto.getId();

        // when
        when(userContactService.findUserContactsById(contactId)).thenReturn(Optional.of(userContactDto));

        // then
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
        // given
        Long contactId = 1000000L;

        // when
        when(userContactService.findUserContactsById(contactId)).thenReturn(Optional.empty());

        // then
        mockMvc.perform(get(BASE_URL + "/contacts/{id}", contactId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Failed entity search"))
                .andExpect(jsonPath("$.errors.cause")
                        .value("User contact with ID: " + contactId + ", not found"));

        verify(userContactService).findUserContactsById(contactId);
    }

    @Test
    public void whenGetUserContactByUserIdExists_thenShouldReturnContact() throws Exception {
        // given
        UserContactDto userContactDto = createUserContactDto();
        UserDto userDto = createUserDtoWithId();

        userDto.setUserContactId(userContactDto.getId());
        Long userDtoId = userDto.getId();

        // when
        when(userContactService.findUserContactByUserId(userDtoId))
                .thenReturn(Optional.of(userContactDto));

        // then
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
        // given
        Long nonExistingUserDtoId = 1000000L;

        // when
        when(userContactService.findUserContactByUserId(nonExistingUserDtoId))
                .thenReturn(Optional.empty());

        // then
        mockMvc.perform(get(BASE_URL + "/{id}/contacts", nonExistingUserDtoId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Failed entity search"))
                .andExpect(jsonPath("$.errors.cause")
                        .value("User contact with ID: " + nonExistingUserDtoId + ", not found"));

        verify(userContactService).findUserContactByUserId(nonExistingUserDtoId);
    }

    @Test
    public void whenUpdateUserContactByUserId_thenReturnNoContent() throws Exception {
        // given
        Long userDtoId = 1L;
        UserContactDto userContactDto = createUserContactDto();

        // when-then
        mockMvc.perform(put(BASE_URL + "/{id}/contacts", userDtoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userContactDto)))
                .andExpect(status().isNoContent());

        verify(userContactService).updateContacts(userContactDto, 1L);
    }

    @Test
    public void whenUpdateInvalidUserContact_thenReturnBadRequest() throws Exception {
        // given
        Long userDtoId = 1L;
        UserContactDto userContactDto = new UserContactDto();
        userContactDto.setTelegramId("invalidTelegramId");
        userContactDto.setMobilePhone("123");

        // when-then
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
                .userRole(Role.MANAGER)
                .build();
    }

    private static UserDto createUserDtoWithNullId() {
        return UserDto.builder()
                .firstName("Pole")
                .lastName("Smith")
                .email("ps@test.com")
                .userContactId(1L)
                .userRole(Role.MANAGER)
                .build();
    }

    private static UserContactDto createUserContactDto() {
        return UserContactDto.builder()
                .id(1L)
                .telegramId("@valid_id")
                .mobilePhone("+1234567890")
                .build();
    }

    private static List<UserDto> createListOfUserDto(int userDtoCount, Role role) {
        return IntStream
                .range(0, userDtoCount)
                .mapToObj(i -> new UserDto(
                        (long) i,
                        "Pole" + i,
                        "Smith" + i,
                        "test" + i + "@test.com",
                        (long) i,
                        role
                ))
                .toList();
    }
}
