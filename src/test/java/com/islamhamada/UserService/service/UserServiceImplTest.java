package com.islamhamada.UserService.service;

import com.islamhamada.UserService.entity.User;
import com.islamhamada.UserService.exception.UserServiceException;
import com.islamhamada.UserService.model.StoreUserRequest;
import com.islamhamada.UserService.model.UpdateUserRequest;
import com.islamhamada.UserService.repository.UserRepository;
import com.islamhamada.petshop.contracts.dto.UserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    UserService userService = new UserServiceImpl();

    @Nested
    @DisplayName("storeUser")
    class storeUser {

        @Test
        @DisplayName("success 1")
        void storeUser_success1() {
            StoreUserRequest mockStoreUserRequest = getStoreUserRequestMock();
            User user = getUserMock();

            when(userRepository.findByAuth0Id(mockStoreUserRequest.getAuth0_id()))
                    .thenReturn(Optional.of(user));

            long rv = userService.storeUser(mockStoreUserRequest);

            verify(userRepository, times(1))
                    .findByAuth0Id(anyString());
            verify(userRepository, never())
                    .save(any());

            assertEquals(rv, user.getId());
        }

        @Test
        @DisplayName("success 2")
        void storeUser_success2() {
            StoreUserRequest mockStoreUserRequest = getStoreUserRequestMock();

            when(userRepository.findByAuth0Id(mockStoreUserRequest.getAuth0_id()))
                    .thenReturn(Optional.empty());
            User[] createdUser = {null};
            when(userRepository.save(any(User.class)))
                    .thenAnswer(invocation -> {
                        User user = invocation.getArgument(0);
                        createdUser[0] = user;
                        return user;
                    });

            long rv = userService.storeUser(mockStoreUserRequest);

            verify(userRepository, times(1))
                    .findByAuth0Id(anyString());
            verify(userRepository, times(1))
                    .save(any());

            assertEquals(createdUser[0].getEmail(), mockStoreUserRequest.getEmail());
            assertEquals(createdUser[0].getAuth0Id(), mockStoreUserRequest.getAuth0_id());
            assertEquals(createdUser[0].getUsername(), mockStoreUserRequest.getUsername());
        }

        private StoreUserRequest getStoreUserRequestMock() {
            User user = getUserMock();
            return StoreUserRequest.builder()
                    .email(user.getEmail())
                    .auth0_id(user.getAuth0Id())
                    .username(user.getUsername())
                    .build();
        }
    }

    @Nested
    @DisplayName("getUser")
    class getUser {

        @Test
        @DisplayName("success")
        void getUser_success() {
            User user = getUserMock();

            when(userRepository.findById(anyLong()))
                    .thenReturn(Optional.of(user));

            UserDTO userDTO = userService.getUser(user.getId());

            verify(userRepository, times(1))
                    .findById(anyLong());

            assertNotNull(userDTO);
            assertEquals(user.getUsername(), userDTO.getUsername());
            assertEquals(user.getEmail(), userDTO.getEmail());
            assertEquals(user.getCountry(), userDTO.getCountry());
            assertEquals(user.getCity(), userDTO.getCity());
            assertEquals(user.getStreet(), userDTO.getStreet());
            assertEquals(user.getHouseNumber(), userDTO.getHouseNumber());
            assertEquals(user.getFirstName(), userDTO.getFirstName());
            assertEquals(user.getLastName(), userDTO.getLastName());
            assertEquals(user.getCreatedAt(), userDTO.getCreatedAt());
            assertEquals(user.getPhoneNumber(), userDTO.getPhoneNumber());
            assertEquals(user.getPostalCode(), userDTO.getPostalCode());
            assertEquals(user.getStreet(), userDTO.getStreet());
        }

        @Test
        @DisplayName("failure")
        void getUser_failure() {
            when(userRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            int id = 1;
            UserServiceException exception = assertThrows(UserServiceException.class,
                    () -> userService.getUser(id));

            verify(userRepository, times(1))
                    .findById(anyLong());

            assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
            assertEquals("No user found with id: " + id, exception.getMessage());
            assertEquals("USER_NOT_FOUND", exception.getError_code());
        }
    }

    @Nested
    @DisplayName("updateUser")
    class updateUser {

        @Test
        void updateUser_success() {
            int id = 1;
            User user = getUserMock();
            UpdateUserRequest updateUserRequest = getUpdateUserRequestMock();

            when(userRepository.findById(anyLong()))
                    .thenReturn(Optional.of(user));
            when(userRepository.save(any()))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            UserDTO userDTO = userService.updateUser(id, updateUserRequest);

            verify(userRepository, times(1))
                    .findById(anyLong());
            verify(userRepository, times(1))
                    .save(any());

            assertEquals(user.getUsername(), userDTO.getUsername());
            assertEquals(user.getEmail(), userDTO.getEmail());
            assertEquals(user.getCreatedAt(), userDTO.getCreatedAt());
            assertEquals(updateUserRequest.getFirstName(), userDTO.getFirstName());
            assertEquals(updateUserRequest.getLastName(), userDTO.getLastName());
            assertEquals(updateUserRequest.getPhoneNumber(), userDTO.getPhoneNumber());
            assertEquals(updateUserRequest.getCountry(), userDTO.getCountry());
            assertEquals(updateUserRequest.getCity(), userDTO.getCity());
            assertEquals(updateUserRequest.getPostalCode(), userDTO.getPostalCode());
            assertEquals(updateUserRequest.getStreet(), userDTO.getStreet());
            assertEquals(updateUserRequest.getHouseNumber(), userDTO.getHouseNumber());
        }

        @Test
        void updateUser_failure() {
            UpdateUserRequest updateUserRequest = getUpdateUserRequestMock();

            when(userRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            long id = 1;
            UserServiceException exception = assertThrows(UserServiceException.class,
                    () -> userService.updateUser(id, updateUserRequest));

            verify(userRepository, times(1))
                    .findById(anyLong());
            verify(userRepository, never())
                    .save(any());

            assertEquals("No user found with id: " + id, exception.getMessage());
            assertEquals("USER_NOT_FOUND", exception.getError_code());
            assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        }

        private UpdateUserRequest getUpdateUserRequestMock() {
            return UpdateUserRequest.builder()
                    .firstName("firstName 2")
                    .lastName("lastName 2")
                    .phoneNumber("phoneNumber 2")
                    .country("country 2")
                    .city("city 2")
                    .postalCode("postalCode 2")
                    .street("street 2")
                    .houseNumber("houseNumber 2")
                    .build();
        }
    }

    public User getUserMock() {
        return User.builder()
                .email("email")
                .city("city")
                .country("country")
                .id(1)
                .createdAt(Instant.now())
                .firstName("firstName")
                .lastName("lastName")
                .auth0Id("auth0_id")
                .phoneNumber("phoneNumber")
                .houseNumber("houseNumber")
                .street("street")
                .postalCode("postalCode")
                .build();
    }
}