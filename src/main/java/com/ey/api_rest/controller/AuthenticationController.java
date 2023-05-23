package com.ey.api_rest.controller;

import com.ey.api_rest.dto.security.JWTTokenDTO;
import com.ey.api_rest.dto.user.AuthenticationUserDTO;
import com.ey.api_rest.infra.security.TokenService;
import com.ey.api_rest.model.User;
import com.ey.api_rest.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            TokenService tokenService,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity authenticateUser(@RequestBody @Valid AuthenticationUserDTO authenticationUserDTO) {

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(authenticationUserDTO.name(), authenticationUserDTO.password());

        var authenticatedUser = (User) authenticationManager.authenticate(authenticationToken).getPrincipal();

        // Update last login time
        authenticatedUser.setLastlogin(LocalDateTime.now());
        userRepository.save(authenticatedUser);

        var JWTtoken = tokenService.generateToken(authenticatedUser);

        return ResponseEntity.ok(new JWTTokenDTO(JWTtoken));
    }
}
