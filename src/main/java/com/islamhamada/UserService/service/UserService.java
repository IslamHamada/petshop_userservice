package com.islamhamada.UserService.service;

import com.islamhamada.UserService.entity.User;
import com.islamhamada.UserService.model.StoreUserRequest;
import com.islamhamada.UserService.model.UpdateUserRequest;

public interface UserService {
    long storeUser(StoreUserRequest request);
    User getUser(long userId);
    void updateUser(long user_id, UpdateUserRequest request);
}
