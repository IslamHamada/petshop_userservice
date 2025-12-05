package com.islamhamada.UserService.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreUserRequest {
    @NotBlank
    private String auth0_id;
    @NotBlank
    private String username;
    @NotBlank @Email
    private String email;
}
