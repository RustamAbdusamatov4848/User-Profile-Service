package com.iprody.userprofileservice;

import com.iprody.userprofileservice.dto.UserDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDTOValidationTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenFirstNameTooLong_thenValidationFails() {
        //Given
        UserDto userDTO = new UserDto(1L, "ThisNameIsWayTooLongForTheFieldAsItExceedsFiftyCharacters", "Smith", "pole.smith@example.com");

        //When
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDTO);

        //Then
        assertEquals(1, violations.size());
    }

    @Test
    public void whenLastNameTooLong_thenValidationFails() {
        //Given
        UserDto userDTO = new UserDto(1L, "Pole", "ThisNameIsWayTooLongForTheFieldAsItExceedsFiftyCharacters", "pole.smith@example.com");

        //When
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDTO);

        //Then
        assertEquals(1, violations.size());
    }

    @Test
    public void whenEmailInvalid_thenValidationFails() {
        //Given
        UserDto userDTO = new UserDto(1L, "Pole", "Smith", "invalid-email");

        //When
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDTO);

        //Then
        assertEquals(1, violations.size());
    }

    @Test
    public void whenAllFieldsValid_thenValidationPasses() {
        //Given
        UserDto userDTO = new UserDto(1L, "Pole", "Smith", "pole.smith@example.com");

        //When
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDTO);

        //Then
        assertTrue(violations.isEmpty());
    }
}
