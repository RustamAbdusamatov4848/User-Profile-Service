package com.iprody.userprofileservice.utils.validations;

import com.iprody.userprofileservice.dto.UserDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidationTest {

    public static final int MAX_NAME_LENGTH = 50;
    public static final String LONG_NAME = "a".repeat(MAX_NAME_LENGTH + 1);
    public static final String INVALID_EMAIL = "invalid-email";

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenFirstNameIsTooLong_thenValidationFails() {
        // given
        UserDto userDto = createUserDto(LONG_NAME, "Smith", "pole.smith@test.ru");

        // when
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        // then
        assertEquals(1, violations.size());
        assertInvalidField("firstName", violations);
    }

    @Test
    void whenLastNameIsTooLong_thenValidationFails() {
        // given
        UserDto userDto = createUserDto("Pole", LONG_NAME, "pole.smith@test.ru");

        // when
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        // then
        assertEquals(1, violations.size());
        assertInvalidField("lastName", violations);
    }

    @Test
    void whenEmailIsInvalid_thenValidationFails() {
        // given
        UserDto userDto = createUserDto("Pole", "Smith", INVALID_EMAIL);

        // when
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        // then
        assertEquals(1, violations.size());
        assertInvalidField("email", violations);
    }

    @Test
    void whenAllFieldsAreValid_thenValidationSucceeds() {
        // given
        UserDto userDto = createUserDto("Pole", "Smith", "pole.smith@test.ru");

        // when
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        // then
        assertTrue(violations.isEmpty());
    }

    private static UserDto createUserDto(String firstname, String lastname, String email) {
        return new UserDto(1L, firstname, lastname, email, 1L);
    }

    private static void assertInvalidField(String fieldName, Set<ConstraintViolation<UserDto>> violations) {
        assertEquals(fieldName, violations
                .iterator()
                .next()
                .getPropertyPath()
                .toString());
    }
}
