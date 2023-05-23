package com.ey.api_rest.controller;

import com.ey.api_rest.dto.user.UserCreateDTO;
import com.ey.api_rest.dto.user.UserListDTO;
import com.ey.api_rest.dto.user.UserResponseDataDTO;
import com.ey.api_rest.dto.user.UserUpdateDTO;
import com.ey.api_rest.infra.security.TokenService;
import com.ey.api_rest.model.User;
import com.ey.api_rest.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public UserController(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    /**
     * Nota: Sobre el uso de DTO y Records para este proyecto.
     * Se hace uso de DTOs principalmente para evitar vulneratibilidades como los ataques de Mass Assignment.
     * DTO también proporciona un mayor control sobre los datos que se desean mostrar en JSON. Si bien
     * podría haberse utilizado la anotación @JsonIgnore, no es recomendable ignorar atributos de este modo
     * pues otro endpoint podría ocupar dichos atributos, entonces no es muy flexible. DTO es más flexible,
     * y ahorra tiempo en procesamiento. Con DTO tambièn evitamos el bucle infinito que se provoca al trabajar
     * directamente con entidades JPA que tengan alguna auto-relación o relación bidireccional, que mostraría
     * un error del tipo StackOverFlow al momento en que Spring trate de generar el JSON de ese objeto.
     * Para el caso de este proyecto se utilizan Records para representar los DTO. Record es un recurso
     * lanzado oficialmente en Java 16, pero estuvo disponible experimentalmente desde Java 14 y permite
     * representar clases inmutables que contengan solo atributos, constructores y métodos de lectura, de manera
     * simple y ágil.
     */

    @PostMapping
    public ResponseEntity<UserResponseDataDTO> createUser(@RequestBody @Valid UserCreateDTO userCreateDTO,
                                     UriComponentsBuilder uriComponentsBuilder) {

        User user = userRepository.save(new User(userCreateDTO));

        // Generar el token JWT para el usuario
        String jwtToken = tokenService.generateToken(user);

        // Guardar el token JWT en el usuario
        user.setToken(jwtToken);
        userRepository.save(user);

        UserResponseDataDTO userResponseDataDTO = new UserResponseDataDTO(
                user.getId(),
                user.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                null,
                user.getLastlogin().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                user.getToken(),
                user.getIsactive());

        URI url = uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(url).body(userResponseDataDTO);
    }

    // Los datos se muestran paginados, solo 2 elementos por página, por defecto
    @GetMapping
    public ResponseEntity<Page<UserListDTO>> listUsers(@PageableDefault(size = 3) Pageable pageable){

        //return userRepository.findAll().stream().map(UserListDTO::new).toList();
        //return userRepository.findAll(pageable).map(UserListDTO::new);
        return ResponseEntity.ok(userRepository.findByisactiveTrue(pageable).map(UserListDTO::new));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<UserResponseDataDTO> updateUser(@RequestBody @Valid UserUpdateDTO userUpdateDTO){

        User user = userRepository.getReferenceById(userUpdateDTO.id());
        user.update(userUpdateDTO);
        return ResponseEntity.ok(new UserResponseDataDTO(
                user.getId(),
                user.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                user.getModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                user.getLastlogin().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                user.getToken(),
                user.getIsactive()
        ));
    }

    // Se recomienda utilizar el delete lógico.
    // Pero se deja la funcionalidad para eliminar al usuariuo de la base de datos en caso que
    // así lo requiera el negocio.
    //@DeleteMapping("/{id}")
    //@Transactional
    //public void deleteUser(@PathVariable UUID id){

    //    User user = userRepository.getReferenceById(id);
    //    userRepository.delete(user);
    //}

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity logicalDeleteUser(@PathVariable UUID id){

        User user = userRepository.getReferenceById(id);
        user.inactiveUser();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserListDTO> getUserData(@PathVariable UUID id){

        User user = userRepository.getReferenceById(id);
        var userData = new UserListDTO(user);

        return ResponseEntity.ok(userData);
    }
}