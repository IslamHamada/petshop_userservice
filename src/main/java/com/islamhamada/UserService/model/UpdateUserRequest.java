package com.islamhamada.UserService.model;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String first_name;
    private String last_name;
    private String street;
    private String houseNumber;
    private String city;
    private String postalCode;
    private String country;
    private String phoneNumber;
}
