package com.ey.api_rest.dto.user;

import com.ey.api_rest.dto.phone.PhoneDetailDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserUpdateDTO(
        @NotNull(message = "{id.required}")
        UUID id,
        String name,
        @NotBlank(message = "{email.required}")
        @Email(message = "{email.invalid}")
        String email,
        LocalDateTime modified,
        @Valid
        List<PhoneDetailDTO> phones) {
}
