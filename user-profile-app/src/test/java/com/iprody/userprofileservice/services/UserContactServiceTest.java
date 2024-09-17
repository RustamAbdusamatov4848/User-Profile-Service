package com.iprody.userprofileservice.services;

import com.iprody.userprofileservice.dto.UserContactDto;
import com.iprody.userprofileservice.models.Role;
import com.iprody.userprofileservice.models.User;
import com.iprody.userprofileservice.models.UserContact;
import com.iprody.userprofileservice.repositories.UserContactRepository;
import com.iprody.userprofileservice.repositories.UserRepository;
import com.iprody.userprofileservice.services.impl.UserContactServiceImpl;
import com.iprody.userprofileservice.utils.UserContactMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class UserContactServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserContactRepository userContactRepository;

    @Autowired
    private UserContactMapper userContactMapper;

    @Autowired
    private UserContactServiceImpl userContactService;

    @BeforeEach
    void setUp() {
        userContactRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void whenFindUserContactsById_thenUserContactDtoShouldBeReturned() {
        // given
        UserContactDto expectedUserContactDto = createUserContactDto();

        // when
        Long id = expectedUserContactDto.getId();
        Optional<UserContactDto> actualUserContactDto = userContactService
                .findUserContactsById(id);

        // then
        assertTrue(actualUserContactDto.isPresent());
        assertUserContactDtoEquals(expectedUserContactDto, actualUserContactDto.get());
    }

    @Test
    void whenFindUserContactByUserId_thenUserContactDtoShouldBeReturned() {
        // given
        User savedUser = createUser();
        UserContactDto expectedUserContactDto = userContactMapper
                .contactToContactDto(savedUser.getUserContact());

        // when
        Long userId = savedUser.getId();
        Optional<UserContactDto> actualUserContactDto = userContactService
                .findUserContactByUserId(userId);

        // then
        assertTrue(actualUserContactDto.isPresent());
        assertUserContactDtoEquals(expectedUserContactDto, actualUserContactDto.get());
    }

    @Test
    void whenUpdateContactsByUserId_thenContactsShouldBeUpdated() {
        // given
        User savedUser = createUser();

        // when
        UserContactDto newUserContactDto = createUserContactDto();
        userContactService.updateContacts(newUserContactDto, savedUser.getId());
        Optional<User> updatedUser = userRepository.findById(savedUser.getId());

        // then
        assertTrue(updatedUser.isPresent());
        assertUserContactEquals(newUserContactDto, updatedUser.get().getUserContact());
    }

    private User createUser() {
        UserContact userContact = new UserContact();
        userContact.setTelegramId("@valid-id");
        userContact.setMobilePhone("+1234567890");

        User user = new User();
        user.setFirstName("Pole");
        user.setLastName("Smith");
        user.setEmail("pole.smith@test.ru");
        user.setUserContact(userContact);
        user.setUserRole(Role.MANAGER);

        return userRepository.save(user);
    }

    private UserContactDto createUserContactDto() {
        UserContactDto userContactDto = buildUserContactDto();

        UserContact savedUserContact = userContactRepository
                .save(userContactMapper.contactDtoToContact(userContactDto));

        return userContactMapper.contactToContactDto(savedUserContact);
    }

    private UserContactDto buildUserContactDto() {
        return UserContactDto.builder()
                .telegramId("@newTelegramId")
                .mobilePhone("+0987654321")
                .build();
    }

    private void assertUserContactDtoEquals(UserContactDto expected, UserContactDto actual) {
        assertThat(actual)
                .extracting(
                        UserContactDto::getId,
                        UserContactDto::getTelegramId,
                        UserContactDto::getMobilePhone
                )
                .containsExactly(
                        expected.getId(),
                        expected.getTelegramId(),
                        expected.getMobilePhone()
                );
    }

    private void assertUserContactEquals(UserContactDto expected, UserContact actual) {
        assertThat(actual)
                .extracting(UserContact::getTelegramId, UserContact::getMobilePhone)
                .containsExactly(expected.getTelegramId(), expected.getMobilePhone());
    }
}
