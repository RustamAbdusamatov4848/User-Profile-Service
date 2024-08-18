package com.iprody.userprofileservice.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserContactDto {
    private Long id;
    @Pattern(regexp = "^@[a-zA-Z0-9_]{4,}$", message = "Invalid telegram ID format")
    private String telegramId;
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Invalid phone format")
    private String mobilePhone;
}
