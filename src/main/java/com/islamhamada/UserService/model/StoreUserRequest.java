package com.islamhamada.UserService.model;

import lombok.Data;

@Data
public class StoreUserRequest {
    private String auth0_id;
    private String username;
    private String email;
}
