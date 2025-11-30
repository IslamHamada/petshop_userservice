package com.islamhamada.UserService.service;

import com.islamhamada.UserService.entity.User;
import com.islamhamada.UserService.model.StoreUserRequest;
import com.islamhamada.UserService.model.UpdateUserRequest;
import com.islamhamada.UserService.repository.UserRepository;
import com.islamhamada.petshop.contracts.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
                    .createdAt(Instant.now())
                    .build();
            user = userRepository.save(user);
            return user.getId();
        }
    }

    @Override
    public UserDTO getUser(long userId) {
        User user = userRepository.findById(userId).get();
        UserDTO userDTO = UserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .country(user.getCountry())
                .city(user.getCity())
                .postalCode(user.getPostalCode())
                .street(user.getStreet())
                .houseNumber(user.getHouseNumber())
                .build();
        return userDTO;
    }

    @Override
    public UserDTO updateUser(long user_id, UpdateUserRequest request) {
        User user = userRepository.findById(user_id).get();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCountry(request.getCountry());
        user.setCity(request.getCity());
        user.setPostalCode(request.getPostalCode());
        user.setStreet(request.getStreet());
        user.setHouseNumber(request.getHouseNumber());
        user.setPhoneNumber(request.getPhoneNumber());
        user = userRepository.save(user);

        UserDTO userDTO = UserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .country(user.getCountry())
                .city(user.getCity())
                .postalCode(user.getPostalCode())
                .street(user.getStreet())
                .houseNumber(user.getHouseNumber())
                .build();
        return userDTO;
    }
}
