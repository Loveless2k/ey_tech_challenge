package com.ey.api_rest.dto.phone;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record PhoneDetailDTO(

        UUID id,
        @NotBlank(message = "{number.required}")
        String number,
        @NotBlank(message = "{citycode.required}")
        String citycode,
        @NotBlank(message = "{countrycode.required}")
        String countrycode) {
}
