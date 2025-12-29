package com.islamhamada.UserService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "auth0_id", unique = true)
    private String auth0Id;

    private String username;

    @Column(unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    private String street;

    @Column(name = "house_number")
    private String houseNumber;

    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    private String country;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "phone_number")
    private String phoneNumber;
}
