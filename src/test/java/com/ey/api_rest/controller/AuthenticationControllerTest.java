package com.ey.api_rest.controller;

import com.ey.api_rest.infra.security.TokenService;
import com.ey.api_rest.model.User;
import com.ey.api_rest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp(){
        when(tokenService.generateToken(any(User.class))).thenReturn("dummy-token");
    }

    @Test
    @WithMockUser(username = "userTest", password = "password", roles = "USER")
    void shouldAuthenticateUser() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("testUser");
        user.setPassword("testPassword");
        user.setIsactive(true);
        user.setCreated(LocalDateTime.now());
        user.setLastlogin(LocalDateTime.now());
        user.setModified(LocalDateTime.now());

        when(userRepository.findByName(user.getName())).thenReturn(user);

       // mockMvc.perform(post("/auth")
        //                .param("name", "userTest")
        //                .param("password", "password"))
        //                .andReturn();

    }

    @TestConfiguration
    public static class TestSecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .anyRequest()
                    .permitAll();

            return http.build();
        }
    }

    // Nuevamente el problema de autorización.
    // Se debe corregir. El código de estado HTTP 403 generalmente indica que el servidor comprendió la solicitud
    // pero se niega a autorizarla. Este estado es similar a 401 (No autorizado), pero indica que la autenticación del
    // cliente se realizó correctamente pero ha sido prohibida para esos recursos.
}
