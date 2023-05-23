package com.ey.api_rest.model;

import com.ey.api_rest.dto.phone.PhoneDetailDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "phones")
@Entity
public class Phone {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String number;
    private String citycode;
    private String countrycode;

    public Phone(PhoneDetailDTO phoneDetailDTO) {
        this.number = phoneDetailDTO.number();
        this.citycode = phoneDetailDTO.citycode();
        this.countrycode = phoneDetailDTO.countrycode();
    }

    public void update(PhoneDetailDTO phoneDetailDTO){
        if (phoneDetailDTO.number() != null){
            this.number = phoneDetailDTO.number();
        }

        if (phoneDetailDTO.citycode() != null){
            this.citycode = phoneDetailDTO.citycode();
        }

        if (phoneDetailDTO.countrycode() != null){
            this.countrycode = phoneDetailDTO.countrycode();
        }
    }
}
