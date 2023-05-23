package com.ey.api_rest.dto.user;

import com.ey.api_rest.dto.phone.PhoneDetailDTO;
import com.ey.api_rest.model.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserListDTO(UUID id, String name, String email, List<PhoneDetailDTO> phones, String created, String modified, String last_login,
                          String token, Boolean isactive) {

    public UserListDTO(User user){

        this(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhones().stream().map(phone -> new PhoneDetailDTO(
                        phone.getId(),
                        phone.getNumber(),
                        phone.getCitycode(),
                        phone.getCountrycode()
                )).collect(Collectors.toList()),
                user.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                user.getModified() != null ? user.getModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null,
                user.getLastlogin().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                user.getToken(),
                user.getIsactive());
    }
}
