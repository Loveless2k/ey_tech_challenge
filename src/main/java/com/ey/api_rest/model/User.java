package com.ey.api_rest.model;

import com.ey.api_rest.dto.phone.PhoneDetailDTO;
import com.ey.api_rest.dto.user.UserCreateDTO;
import com.ey.api_rest.dto.user.UserUpdateDTO;
import com.ey.api_rest.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "users")
@Entity
public class User implements UserDetails{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Phone> phones = new ArrayList<>();

    private LocalDateTime created;
    private LocalDateTime modified;
    private LocalDateTime lastlogin;
    private String token;
    private Boolean isactive;

    public User(UserCreateDTO userCreateDTO) {
        this.phones = new ArrayList<Phone>();
        for(PhoneDetailDTO phoneDetailDTO : userCreateDTO.phones()){
            Phone phone = new Phone(phoneDetailDTO);
            this.phones.add(phone);
        }
        this.name = userCreateDTO.name();
        this.email = userCreateDTO.email();
        this.password = userCreateDTO.password();
        this.gender = userCreateDTO.gender();
        this.created = LocalDateTime.now();
        this.modified = null;
        this.lastlogin = LocalDateTime.now();
        this.isactive = true;
    }

    public User update(UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO.name() != null){
            this.name = userUpdateDTO.name();
        }

        if (userUpdateDTO.email() != null){
            this.email = userUpdateDTO.email();
        }

        this.modified = LocalDateTime.now();

        List<Phone> updatedPhones = new ArrayList<>();

        for (PhoneDetailDTO phoneDetailDTO : userUpdateDTO.phones()){
            boolean existing = false;

            for (Phone phone : this.phones){
                if (phone.getId().equals(phoneDetailDTO.id())){
                    phone.update(phoneDetailDTO);
                    updatedPhones.add(phone);
                    existing = true;
                    break;
                }
            }

            if(!existing){
                Phone phone = new Phone(phoneDetailDTO);
                updatedPhones.add(phone);
            }
        }

        this.phones = updatedPhones;

        return this;
    }

    public void inactiveUser() {
        this.modified = LocalDateTime.now();
        this.isactive = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
