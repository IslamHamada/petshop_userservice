package com.islamhamada.UserService.service;

import com.islamhamada.UserService.entity.User;
import com.islamhamada.UserService.model.StoreUserRequest;

public interface UserService {
    long storeUser(StoreUserRequest request);
    User getUser(long userId);
}
