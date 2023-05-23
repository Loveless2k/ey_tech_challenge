package com.ey.api_rest.dto.user;

import com.ey.api_rest.model.User;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record UserResponseDataDTO(UUID id, String created, String modified, String last_login,
                                  String token, Boolean isactive) {

    public static UserResponseDataDTO fromUser(User user) {
        return new UserResponseDataDTO(
                user.getId(),
                user.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                user.getModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                user.getLastlogin().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                user.getToken(),
                user.getIsactive()
        );
    }
}
