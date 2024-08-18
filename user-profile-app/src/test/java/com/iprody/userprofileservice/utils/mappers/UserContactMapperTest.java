package com.iprody.userprofileservice.utils.mappers;

import com.iprody.userprofileservice.dto.UserContactDto;
import com.iprody.userprofileservice.models.UserContact;
import com.iprody.userprofileservice.utils.UserContactMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserContactMapperTest {

    private UserContactMapper userContactMapper;

    @BeforeEach
    void setUp() {
        userContactMapper = Mappers.getMapper(UserContactMapper.class);
    }

    @Test
    void whenContactToContactDto_thenCorrect() {
        // given
        UserContact userContact = createUserContact();

        // when
        UserContactDto userContactDto = userContactMapper.contactToContactDto(userContact);

        // then
        assertUserContactDtoEqualsUserContact(userContactDto, userContact);
    }

    @Test
    void whenContactDtoToContact_thenCorrect() {
        // given
        UserContactDto userContactDto = createUserContactDto();

        // when
        UserContact userContact = userContactMapper.contactDtoToContact(userContactDto);

        // then
        assertUserContactEqualsUserContactDto(userContact, userContactDto);
    }

    @Test
    void whenUpdateUserContactFromDto_thenCorrect() {
        // given
        UserContact userContact = createUserContact();

        // when
        UserContactDto userContactDto = new UserContactDto();
        userContactDto.setTelegramId("@new_id");
        userContactDto.setMobilePhone("+1122334455");
        userContactMapper.updateUserContactFromDto(userContactDto, userContact);

        // then
        assertUpdateUserContactFromDto(userContactDto, userContact);
    }

    private UserContact createUserContact() {
        UserContact userContact = new UserContact();
        userContact.setId(1L);
        userContact.setTelegramId("@valid_id");
        userContact.setMobilePhone("+1234567890");

        return userContact;
    }

    private UserContactDto createUserContactDto() {
        UserContactDto userContactDto = new UserContactDto();
        userContactDto.setId(1L);
        userContactDto.setTelegramId("@valid_id");
        userContactDto.setMobilePhone("+1234567890");


        return userContactDto;
    }

    private void assertUserContactDtoEqualsUserContact(UserContactDto userContactDto, UserContact userContact) {
        assertEquals(userContact.getId(), userContactDto.getId());
        assertEquals(userContact.getTelegramId(), userContactDto.getTelegramId());
        assertEquals(userContact.getMobilePhone(), userContactDto.getMobilePhone());
    }

    private void assertUserContactEqualsUserContactDto(UserContact userContact, UserContactDto userContactDto) {
        assertEquals(userContactDto.getId(), userContact.getId());
        assertEquals(userContactDto.getTelegramId(), userContact.getTelegramId());
        assertEquals(userContactDto.getMobilePhone(), userContact.getMobilePhone());
    }

    private void assertUpdateUserContactFromDto(UserContactDto userContactDto, UserContact userContact) {
        assertEquals(userContactDto.getTelegramId(), userContact.getTelegramId());
        assertEquals(userContactDto.getMobilePhone(), userContact.getMobilePhone());
    }
}
