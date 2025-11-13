package com.islamhamada.UserService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class User {
    @Id
    @GeneratedValue
    private long numId;

    private String id;

    private String username;
    private String email;
}
