package com.ey.api_rest.dto.user;

import com.ey.api_rest.dto.phone.PhoneDetailDTO;
import com.ey.api_rest.enums.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;

public record UserCreateDTO(
        @NotBlank(message = "{name.required}")
        String name,
        @NotBlank(message = "{email.required}")
        @Email(message = "{email.invalid}")
        String email,
        @NotBlank(message = "{password.required}")
        // Aplica antes de la encriptación
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d.*\\d).*$", message = "{password.invalid}")
        String password,
        @NotNull(message = "{gender.required}")
        Gender gender,
        @NotNull(message = "{phones.required}")
        @Valid
        List<PhoneDetailDTO> phones,
        LocalDate created,
        LocalDate lastLogin) {
}
