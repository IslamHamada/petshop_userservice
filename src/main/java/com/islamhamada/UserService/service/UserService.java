package com.islamhamada.UserService.service;

import com.islamhamada.UserService.model.StoreUserRequest;
import com.islamhamada.UserService.model.UpdateUserRequest;
import com.islamhamada.petshop.contracts.dto.UserDTO;

public interface UserService {
    long storeUser(StoreUserRequest request);
    UserDTO getUser(long userId);
    UserDTO updateUser(long user_id, UpdateUserRequest request);
}
