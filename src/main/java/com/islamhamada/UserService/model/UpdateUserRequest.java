package com.islamhamada.UserService.model;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String street;
    private String houseNumber;
    private String city;
    private String postalCode;
    private String country;
    private String phoneNumber;
}
