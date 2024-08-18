package com.iprody.userprofileservice.utils.validations;

import com.iprody.userprofileservice.dto.UserContactDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserContactValidationTest {

    private static final String INVALID_TELEGRAM_ID_SHORT = "@123";
    private static final String INVALID_TELEGRAM_ID_FORMAT = "@abc#";
    private static final String INVALID_MOBILE_PHONE = "123456";
    private static final String VALID_TELEGRAM_ID = "@valid_id";
    private static final String VALID_MOBILE_PHONE = "+1234567890";

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenTelegramIdIsTooShort_thenValidationFails() {
        // given
        UserContactDto userContactDto = createUserContactDto(INVALID_TELEGRAM_ID_SHORT, VALID_MOBILE_PHONE);

        // when
        Set<ConstraintViolation<UserContactDto>> violations = validator.validate(userContactDto);

        // then
        assertEquals(1, violations.size());

        assertInvalidField("telegramId", violations);
    }

    @Test
    void whenTelegramIdIsInvalidFormat_thenValidationFails() {
        // given
        UserContactDto userContactDto = createUserContactDto(INVALID_TELEGRAM_ID_FORMAT, VALID_MOBILE_PHONE);

        // when
        Set<ConstraintViolation<UserContactDto>> violations = validator.validate(userContactDto);

        // then
        assertEquals(1, violations.size());
        assertInvalidField("telegramId", violations);
    }

    @Test
    void whenMobilePhoneIsInvalidFormat_thenValidationFails() {
        // given
        UserContactDto userContactDto = createUserContactDto(VALID_TELEGRAM_ID, INVALID_MOBILE_PHONE);

        // when
        Set<ConstraintViolation<UserContactDto>> violations = validator.validate(userContactDto);

        // then
        assertEquals(1, violations.size());
        assertInvalidField("mobilePhone", violations);
    }

    @Test
    void whenAllFieldsAreValid_thenValidationSucceeds() {
        // given
        UserContactDto userContactDto = createUserContactDto(VALID_TELEGRAM_ID, VALID_MOBILE_PHONE);

        // when
        Set<ConstraintViolation<UserContactDto>> violations = validator.validate(userContactDto);

        // then
        assertTrue(violations.isEmpty());
    }

    private static UserContactDto createUserContactDto(String telegramId, String mobilePhone) {
        return new UserContactDto(1L, telegramId, mobilePhone);
    }

    private static void assertInvalidField(String fieldName, Set<ConstraintViolation<UserContactDto>> violations) {
        assertEquals(fieldName, violations
                .iterator()
                .next()
                .getPropertyPath()
                .toString());
    }
}
