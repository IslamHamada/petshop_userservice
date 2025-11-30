package com.islamhamada.UserService.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StoreUserRequest {
    @NotBlank
    private String auth0_id;
    @NotBlank
    private String username;
    @NotBlank @Email
    private String email;
}
