package com.islamhamada.UserService.controller;

import com.islamhamada.UserService.model.StoreUserRequest;
import com.islamhamada.UserService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    private ResponseEntity<Long> storeUser(@RequestBody StoreUserRequest request){
        return new ResponseEntity<>(userService.storeUser(request), HttpStatus.OK);
    }
}
