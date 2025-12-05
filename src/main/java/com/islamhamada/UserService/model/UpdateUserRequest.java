package com.islamhamada.UserService.model;

import com.islamhamada.petshop.contracts.validator.PhoneNumberOrEmpty;
import com.islamhamada.petshop.contracts.validator.PostalCodeOrEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String street;
    private String houseNumber;
    private String city;
    @PostalCodeOrEmpty
    private String postalCode;
    private String country;
    @PhoneNumberOrEmpty
    private String phoneNumber;
}
