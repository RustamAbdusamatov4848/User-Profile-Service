
package com.iprody.userprofileservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    @Size(max = 50, message = "Firstname should not be more then 50 characters")
    private String firstName;
    @Size(max = 50, message = "Lastname should not be more then 50 characters")
    private String lastName;
    @Email(message = "Invalid email format")
    private String email;
    private Long userContactId;
}
