
package com.iprody.userprofileservice.dto;


import com.iprody.userprofileservice.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @Email(message = "Invalid email format")
    private String email;
    private Long userContactId;
    @NotNull
    private Role userRole;
}
