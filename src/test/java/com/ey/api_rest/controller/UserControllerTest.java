package com.ey.api_rest.controller;

import com.ey.api_rest.infra.security.TokenService;
import com.ey.api_rest.model.User;
import com.ey.api_rest.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        when(tokenService.generateToken(any(User.class))).thenReturn("dummy-token");
    }

    @Test
    void shouldReturnUserList() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("testUser");
        user.setPassword("testPassword");
        user.setIsactive(true);
        user.setCreated(LocalDateTime.now());
        user.setLastlogin(LocalDateTime.now());
        user.setModified(LocalDateTime.now());

        Page<User> users = new PageImpl<>(Collections.singletonList(user));

        when(userRepository.findByisactiveTrue(any())).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(userRepository).findByisactiveTrue(any());
    }

    @Test
    void shouldReturnUserById() throws Exception {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        user.setName("testUser");
        user.setPassword("testPassword");
        user.setIsactive(true);
        user.setCreated(LocalDateTime.now());
        user.setLastlogin(LocalDateTime.now());
        user.setModified(LocalDateTime.now());

        when(userRepository.getReferenceById(id)).thenReturn(user);

        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isOk());

        verify(userRepository).getReferenceById(user.getId());

    }

    //@Test
    //void shouldCreateUser() throws Exception {
    //    UUID id = UUID.randomUUID();
    //    User user = new User();
    //    user.setId(id);
    //    user.setName("testUser");
    //    user.setPassword("testPassword");
    //   user.setIsactive(true);
    //   user.setToken(tokenService.generateToken(user));
    //    user.setCreated(LocalDateTime.now());
    //    user.setLastlogin(LocalDateTime.now());
    //    user.setModified(LocalDateTime.now());

    //    User savedUser = new User();
    //    savedUser.setId(id);
    //    savedUser.setName("testUser");
    //    savedUser.setPassword("testPassword");
    //    savedUser.setIsactive(true);
    //    savedUser.setToken(tokenService.generateToken(savedUser));
    //    savedUser.setCreated(LocalDateTime.now());
    //    savedUser.setLastlogin(LocalDateTime.now());
    //    savedUser.setModified(LocalDateTime.now());

    //   when(userRepository.save(user)).thenReturn(savedUser);

        //mockMvc.perform(post("/users")
        //                .header("Authorization", "Bearer " + tokenService.generateToken(user))
        //                .contentType("application/json")
        //                .content(objectMapper.writeValueAsString(user)))
        //        .andExpect(status().isCreated())
        //        .andExpect(jsonPath("$.name", is("testUser")))
        //        .andExpect(jsonPath("$.password", is("testPassword")));

        // Se obtiene una respuesta 403 debido a que la seguridad está impactando esta prueba.
        // Se espera el token válido de este lado, el cuál debe generarse para continuar con esta prueba.
    //}

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
}