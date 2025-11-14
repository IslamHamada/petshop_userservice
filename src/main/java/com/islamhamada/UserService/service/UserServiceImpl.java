package com.islamhamada.UserService.service;

import com.islamhamada.UserService.entity.User;
import com.islamhamada.UserService.model.StoreUserRequest;
import com.islamhamada.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public long storeUser(StoreUserRequest request) {
        Optional<User> userOptional = userRepository.findByAuth0Id(request.getAuth0_id());
        if(userOptional.isPresent()){
            return userOptional.get().getId();
        } else {
            User user = User.builder()
                    .auth0Id(request.getAuth0_id())
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .build();
            user = userRepository.save(user);
            return user.getId();
        }
    }
}
