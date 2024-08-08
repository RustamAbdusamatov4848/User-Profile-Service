package com.iprody.userprofileservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @Size(max = 50)
    private String firstName;
    @Size(max = 50)
    private String lastName;
    @Email
    private String email;
    private Long userContactId;
}
