package com.islamhamada.UserService.service;

import com.islamhamada.UserService.entity.User;
import com.islamhamada.UserService.exception.UserServiceException;
import com.islamhamada.UserService.model.StoreUserRequest;
import com.islamhamada.UserService.model.UpdateUserRequest;
import com.islamhamada.UserService.repository.UserRepository;
import com.islamhamada.petshop.contracts.dto.UserDTO;
import com.islamhamada.petshop.contracts.model.KafkaUserMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    KafkaTemplate<String, KafkaUserMessage> kafkaTemplate;

    @Autowired
    UserRepository userRepository;

    @Override
    public long storeUser(StoreUserRequest request) {
        log.info("Storing user with authId: " + request.getAuth0_id());
        Optional<User> userOptional = userRepository.findByAuth0Id(request.getAuth0_id());
        if(userOptional.isPresent()){
            log.info("User already existed beforehand; No need to store.");
            return userOptional.get().getId();
        } else {
            User user = User.builder()
                    .auth0Id(request.getAuth0_id())
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .createdAt(Instant.now())
                    .build();
            user = userRepository.save(user);
            kafkaTemplate.send("notification", KafkaUserMessage.builder()
                    .message("User " + user.getUsername() + " successfully registered")
                    .userId(user.getId())
                    .build());
            log.info("User successfully stored with id: " + user.getId());
            return user.getId();
        }
    }

    @Override
    public UserDTO getUser(long userId) {
        log.info("Getting user with id: " + userId);
        User user = userRepository.findById(userId).orElseThrow(() ->
            new UserServiceException("No user found with id: " + userId, "NOT_FOUND", HttpStatus.NOT_FOUND));
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
        log.info("User successfully fetched");
        return userDTO;
    }

    @Override
    public UserDTO updateUser(long user_id, UpdateUserRequest request) {
        log.info("Updating user profile with id: " + user_id);
        User user = userRepository.findById(user_id).orElseThrow(() ->
                new UserServiceException("No user found with id: " + user_id, "NOT_FOUND", HttpStatus.NOT_FOUND));
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
        log.info("User successfully updated");
        return userDTO;
    }

    @Override
    public String getUsername(long userId) {
        log.info("Fetching username for user with id: " + userId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserServiceException("No user with given id: " + userId, "NOT_FOUND", HttpStatus.NOT_FOUND)
        );
        log.info("Username already fetched");
        return user.getUsername();
    }
}
