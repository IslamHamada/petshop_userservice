package com.islamhamada.UserService.controller;

import com.islamhamada.UserService.entity.User;
import com.islamhamada.UserService.model.StoreUserRequest;
import com.islamhamada.UserService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PreAuthorize("hasAnyRole('Customer')")
    @PostMapping
    public ResponseEntity<Long> storeUser(@RequestBody StoreUserRequest request){
        return new ResponseEntity<>(userService.storeUser(request), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('Customer')")
    @PostMapping("/{user_id}")
    public ResponseEntity<User> getUser(@PathVariable long user_id){
        return new ResponseEntity<>(userService.getUser(user_id), HttpStatus.OK);
    }
}
