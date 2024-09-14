package com.iprody.userprofileservice.services;

import com.iprody.userprofileservice.dto.UserContactDto;
import com.iprody.userprofileservice.dto.UserDto;
import com.iprody.userprofileservice.models.UserContact;
import com.iprody.userprofileservice.repositories.UserContactRepository;
import com.iprody.userprofileservice.repositories.UserRepository;
import com.iprody.userprofileservice.services.impl.UserServiceImpl;
import com.iprody.userprofileservice.utils.UserContactMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserContactRepository userContactRepository;

    @Autowired
    private UserContactMapper userContactMapper;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void whenFindUserById_thenUserDtoShouldBeReturned() {
        // given
        UserDto userDto = createValidUserDto();
        UserDto createdUserDto = userService.createUser(userDto);

        // when
        Optional<UserDto> foundUserDto = userService.findUserById(createdUserDto.getId());

        // then
        assertTrue(foundUserDto.isPresent());
        assertDtoEquals(createdUserDto, foundUserDto.get());
    }

    @Test
    void whenCreateUser_thenOnlyOneUserDtoShouldBeReturned() {
        // given
        UserDto userDto = createValidUserDto();

        // when
        userService.createUser(userDto);

        // then
        int actualSize = userRepository.findAll().size();
        assertEquals(1, actualSize);
    }

    @Test
    void whenCreateUser_thenUserDtoShouldBeReturned() {
        // given
        UserDto expected = createValidUserDto();

        // when
        UserDto actual = userService.createUser(expected);

        // then
        assertCreated(actual, expected);
    }

    @Test
    void whenUpdateUser_thenUserShouldBeUpdated() {
        // given
        UserDto userDto = createValidUserDto();
        userDto = userService.createUser(userDto);

        // when
        UserDto newUserDto = UserDto
                .builder()
                .id(userDto.getId())
                .firstName("NewName")
                .lastName("NewLastName")
                .email("new@userDto.ru")
                .userContactId(userDto.getUserContactId())
                .build();

        userService.updateUser(newUserDto);

        // then
        Optional<UserDto> updatedDto = userService.findUserById(userDto.getId());

        assertTrue(updatedDto.isPresent());
        assertDtoEquals(newUserDto, updatedDto.get());
    }

    @Test
    void whenDeleteUserById_thenUserShouldBeDeleted() {
        // given
        UserDto userDto = createValidUserDto();
        UserDto userSaved = userService.createUser(userDto);

        // when
        Long id = userSaved.getId();
        userService.deleteUserById(id);

        // then
        assertFalse(userService.findUserById(id).isPresent());
    }

    @Test
    void whenCheckIsExistUser_thenShouldReturnTrue() {
        // given
        UserDto userDto = createValidUserDto();
        UserDto createdUserDto = userService.createUser(userDto);
        Long userExistId = createdUserDto.getId();

        // when
        boolean foundUser = userService.isExistUser(userExistId);

        // then
        assertTrue(foundUser);
    }

    private UserDto createValidUserDto() {
        UserContactDto userContactDto = createValidUserContactDto();

        UserContact savedUserContact = userContactRepository
                .save(userContactMapper.contactDtoToContact(userContactDto));

        return UserDto
                .builder()
                .firstName("Pole")
                .lastName("Smith")
                .email("pole.smith@test.ru")
                .userContactId(savedUserContact.getId())
                .build();
    }

    private UserContactDto createValidUserContactDto() {
        return UserContactDto
                .builder()
                .telegramId("@valid_id")
                .mobilePhone("+1234567890")
                .build();
    }

    private void assertDtoEquals(UserDto expected, UserDto actual) {
        assertThat(actual)
                .extracting(
                        UserDto::getId,
                        UserDto::getFirstName,
                        UserDto::getLastName,
                        UserDto::getEmail,
                        UserDto::getUserContactId
                )
                .containsExactly(
                        expected.getId(),
                        expected.getFirstName(),
                        expected.getLastName(),
                        expected.getEmail(),
                        expected.getUserContactId()
                );
    }

    private static void assertCreated(UserDto actual, UserDto expected) {
        assertNotNull(actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getUserContactId(), actual.getUserContactId());
    }
}
