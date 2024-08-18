package com.iprody.userprofileservice.utils.mappers;


import com.iprody.userprofileservice.dto.UserDto;
import com.iprody.userprofileservice.models.User;
import com.iprody.userprofileservice.models.UserContact;
import com.iprody.userprofileservice.utils.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void whenUserToUserDto_thenCorrect() {
        // given
        User user = createUser();

        // when
        UserDto userDto = userMapper.userToUserDto(user);

        // then
        assertUserDtoEqualsUser(user, userDto);
    }


    @Test
    void whenUserDtoToUser_thenCorrect() {
        // given
        UserDto userDto = createUserDto();

        // when
        User user = userMapper.userDtoToUser(userDto);

        // then
        assertUserEqualsUserDto(userDto, user);
    }


    @Test
    void whenUpdateUserFromDto_thenCorrect() {
        // given
        User user = createUser();

        // when
        UserDto userDto = new UserDto();
        userDto.setFirstName("NewFirstName");
        userDto.setLastName("NewLastName");
        userDto.setEmail("new.email@example.com");
        userMapper.updateUserFromDto(userDto, user);

        // then
        assertUpdateUserFromDto(userDto, user);
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Pole");
        user.setLastName("Smith");
        user.setEmail("pole.smith@test.ru");
        user.setUserContact(new UserContact());
        user.getUserContact().setId(2L);

        return user;
    }

    private UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("Pole");
        userDto.setLastName("Smith");
        userDto.setEmail("pole.smith@test.ru");
        userDto.setUserContactId(2L);

        return userDto;
    }

    private void assertUserDtoEqualsUser(User user, UserDto userDto) {
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getUserContactId(), user.getUserContact().getId());
    }

    private void assertUserEqualsUserDto(UserDto userDto, User user) {
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getUserContact().getId(), userDto.getUserContactId());
    }

    private void assertUpdateUserFromDto(UserDto userDto, User user) {
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }
}
