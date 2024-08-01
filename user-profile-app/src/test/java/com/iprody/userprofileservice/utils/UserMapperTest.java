package com.iprody.userprofileservice.utils;

import com.iprody.userprofileservice.dto.UserDto;
import com.iprody.userprofileservice.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserMapperTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void givenUser_whenUserToUserDTO_thenCorrect() {
        //Given
        User user = new User(1L, "Pole", "Smith", "pole.smith@example.com");

        //When
        UserDto userDTO = userMapper.userToUserDto(user);

        //Then
        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getFirstName(), userDTO.getFirstName());
        assertEquals(user.getLastName(), userDTO.getLastName());
        assertEquals(user.getEmail(), userDTO.getEmail());

    }

    @Test
    public void givenUserDTO_whenUserDTOToUser_thenCorrect() {
        //Given
        UserDto userDTO = new UserDto(1L, "Pole", "Smith", "pole.smith@example.com");

        //When
        User user = userMapper.userDtoToUser(userDTO);

        //Then
        assertNotNull(user);
        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getFirstName(), user.getFirstName());
        assertEquals(userDTO.getLastName(), user.getLastName());
        assertEquals(userDTO.getEmail(), user.getEmail());
    }
}
