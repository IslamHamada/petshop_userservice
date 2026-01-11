package com.islamhamada.UserService.controller;

import com.islamhamada.UserService.model.StoreUserRequest;
import com.islamhamada.UserService.model.UpdateUserRequest;
import com.islamhamada.UserService.service.UserService;
import com.islamhamada.petshop.contracts.dto.UserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
    @PostMapping("/protected")
    public ResponseEntity<Long> storeUser(@Valid @RequestBody StoreUserRequest request){
        return new ResponseEntity<>(userService.storeUser(request), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('Customer')")
    @GetMapping("/protected/{user_id}")
    public ResponseEntity<UserDTO> getUser(@PositiveOrZero @PathVariable long user_id){
        return new ResponseEntity<>(userService.getUser(user_id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('Customer')")
    @PutMapping("/protected/{user_id}")
    public ResponseEntity<UserDTO> updateUser(@PositiveOrZero @PathVariable long user_id, @Valid @RequestBody UpdateUserRequest request){
        UserDTO user = userService.updateUser(user_id, request);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/public/username/{userId}")
    public ResponseEntity<String> getUsername(@PositiveOrZero @PathVariable long userId){
        return new ResponseEntity<>(userService.getUsername(userId), HttpStatus.OK);
    }
}
