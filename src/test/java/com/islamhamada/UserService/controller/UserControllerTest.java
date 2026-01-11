package com.islamhamada.UserService.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.islamhamada.UserService.entity.User;
import com.islamhamada.UserService.model.StoreUserRequest;
import com.islamhamada.UserService.model.UpdateUserRequest;
import com.islamhamada.UserService.repository.UserRepository;
import com.islamhamada.petshop.contracts.dto.UserDTO;
import com.islamhamada.petshop.contracts.model.RestExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableConfigurationProperties
class UserControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    SimpleGrantedAuthority adminRole = new SimpleGrantedAuthority("ROLE_Admin");
    SimpleGrantedAuthority customerRole = new SimpleGrantedAuthority("ROLE_Customer");

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Nested
    public class storeUser {

        SimpleGrantedAuthority neededRole = customerRole;
        SimpleGrantedAuthority notNeededRule = adminRole;

        @Test
        public void success_new_user() throws Exception {
            StoreUserRequest storeUserRequest = getMockStoreUserRequest();
            MvcResult mvcResult = mockMvc.perform(post("/user/protected")
                    .with(jwt().authorities(neededRole))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(storeUserRequest))
            ).andExpect(status().isOk())
                .andReturn();
            String response = mvcResult.getResponse().getContentAsString();
            User user = userRepository.findByAuth0Id(storeUserRequest.getAuth0_id()).get();
            assertEquals(user.getId() + "", response);
            assertEquals(storeUserRequest.getAuth0_id(), user.getAuth0Id());
            assertEquals(storeUserRequest.getUsername(), user.getUsername());
            assertEquals(storeUserRequest.getEmail(), user.getEmail());
        }

        @Test
        public void success_old_user() throws Exception {
            User user = getMockUser();
            userRepository.save(user);
            StoreUserRequest storeUserRequest = getMockStoreUserRequest();
            MvcResult mvcResult = mockMvc.perform(post("/user/protected")
                            .with(jwt().authorities(neededRole))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(storeUserRequest))
                    ).andExpect(status().isOk())
                    .andReturn();
            String response = mvcResult.getResponse().getContentAsString();
            User user2 = userRepository.findByAuth0Id(storeUserRequest.getAuth0_id()).get();
            assertEquals(user.getId() + "", response);
            assertEquals(storeUserRequest.getAuth0_id(), user.getAuth0Id());
            assertEquals(storeUserRequest.getUsername(), user.getUsername());
            assertEquals(storeUserRequest.getEmail(), user.getEmail());

            user.setCreatedAt(Instant.ofEpochMilli(user.getCreatedAt().toEpochMilli()));
            user2.setCreatedAt(Instant.ofEpochMilli(user2.getCreatedAt().toEpochMilli()));

            assertThat(user)
                    .usingRecursiveComparison()
                    .isEqualTo(user2);
        }

        @Test
        public void failure_no_permission() throws Exception {
            StoreUserRequest storeUserRequest = getMockStoreUserRequest();
            mockMvc.perform(post("/user/protected")
                    .with(jwt().authorities(notNeededRule))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(storeUserRequest))
            ).andExpect(status().isForbidden())
                    .andReturn();
        }

        @ParameterizedTest
        @MethodSource("bad_input")
        public void failure_bad_input(StoreUserRequest storeUserRequest) throws Exception {
            mockMvc.perform(post("/user/protected")
                            .with(jwt().authorities(neededRole))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(storeUserRequest))
                    ).andExpect(status().isBadRequest())
                    .andReturn();
        }

        public static List<StoreUserRequest> bad_input() {
            List<StoreUserRequest> list = new ArrayList<>();
            list.add(StoreUserRequest.builder()
                    .email("email")
                    .auth0_id("auth0_id")
                    .username("username")
                    .build());
            list.add(StoreUserRequest.builder()
                    .email("")
                    .auth0_id("auth0_id")
                    .username("username")
                    .build());
            list.add(StoreUserRequest.builder()
                    .email("email@email.com")
                    .auth0_id("")
                    .username("username")
                    .build());
            list.add(StoreUserRequest.builder()
                    .email("email@email.com")
                    .auth0_id("auth0_id")
                    .username("")
                    .build());
            return list;
        }

        private StoreUserRequest getMockStoreUserRequest() {
            return StoreUserRequest.builder()
                    .email("email@email.com")
                    .username("username")
                    .auth0_id("auth0_id")
                    .build();
        }
    }

    @Nested
    public class getUser {

        SimpleGrantedAuthority neededRole = new SimpleGrantedAuthority("ROLE_Customer");
        SimpleGrantedAuthority notNeededRole = new SimpleGrantedAuthority("ROLE_Admin");

        @Test
        public void success() throws Exception {
            User user = getMockUser();
            user = userRepository.save(user);
            MvcResult mvcResult = mockMvc.perform(get("/user/protected/" + user.getId())
                            .with(jwt().authorities(neededRole))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(user))
                    ).andExpect(status().isOk())
                    .andReturn();
            String response = mvcResult.getResponse().getContentAsString();
            UserDTO user2 = objectMapper.readValue(response, UserDTO.class);
            assertEquals(user.getCity(), user2.getCity());
            assertEquals(user.getCountry(), user2.getCountry());
            assertEquals(user.getStreet(), user2.getStreet());
            assertEquals(user.getCreatedAt().toEpochMilli(), user2.getCreatedAt().toEpochMilli());
            assertEquals(user.getFirstName(), user2.getFirstName());
            assertEquals(user.getLastName(), user2.getLastName());
            assertEquals(user.getHouseNumber(), user2.getHouseNumber());
            assertEquals(user.getEmail(), user2.getEmail());
            assertEquals(user.getPhoneNumber(), user2.getPhoneNumber());
            assertEquals(user.getPostalCode(), user2.getPostalCode());
            assertEquals(user.getUsername(), user2.getUsername());
        }

        @Test
        public void failure_no_user() throws Exception {
            long user_id = 1;
            MvcResult mvcResult = mockMvc.perform(get("/user/protected/" + user_id)
                            .with(jwt().authorities(neededRole))
                    ).andExpect(status().isNotFound())
                    .andReturn();
            String response = mvcResult.getResponse().getContentAsString();
            RestExceptionResponse exceptionResponse = objectMapper.readValue(response, RestExceptionResponse.class);
            assertEquals("USER_NOT_FOUND", exceptionResponse.getError_code());
            assertEquals("No user found with id: " + user_id, exceptionResponse.getError_message());
        }

        @Test
        public void failure_no_permission() throws Exception {
            long user_id = 1;
            mockMvc.perform(get("/user/protected/" + user_id)
                            .with(jwt().authorities(notNeededRole))
                    ).andExpect(status().isForbidden())
                    .andReturn();
        }

        @Test
        public void failure_bad_input() throws Exception {
            long user_id = -1;
            mockMvc.perform(get("/user/protected/" + user_id)
                            .with(jwt().authorities(notNeededRole))
                    ).andExpect(status().isBadRequest())
                    .andReturn();
        }
    }

    @Nested
    public class updateUser {
        SimpleGrantedAuthority neededRole = new SimpleGrantedAuthority("ROLE_Customer");
        SimpleGrantedAuthority notNeededRole = new SimpleGrantedAuthority("ROLE_Admin");

        @Test
        public void success() throws Exception {
            long user_id = 1;
            User user = getMockUser();
            userRepository.save(user);
            UpdateUserRequest request = getMockUpdateUserRequest();
            MvcResult mvcResult = mockMvc.perform(put("/user/protected/" + user_id)
                            .with(jwt().authorities(neededRole))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request))
                    ).andExpect(status().isOk())
                    .andReturn();
            String response = mvcResult.getResponse().getContentAsString();
            UserDTO user2 = objectMapper.readValue(response, UserDTO.class);

            assertEquals(user.getCreatedAt().toEpochMilli(), user2.getCreatedAt().toEpochMilli());
            assertEquals(user.getEmail(), user2.getEmail());
            assertEquals(user.getUsername(), user2.getUsername());

            assertEquals(request.getCity(), user2.getCity());
            assertEquals(request.getCountry(), user2.getCountry());
            assertEquals(request.getStreet(), user2.getStreet());
            assertEquals(request.getFirstName(), user2.getFirstName());
            assertEquals(request.getLastName(), user2.getLastName());
            assertEquals(request.getHouseNumber(), user2.getHouseNumber());
            assertEquals(request.getPhoneNumber(), user2.getPhoneNumber());
            assertEquals(request.getPostalCode(), user2.getPostalCode());
        }

        @Test
        public void failure_no_user() throws Exception {
            long user_id = 1;
            UpdateUserRequest request = getMockUpdateUserRequest();
            MvcResult mvcResult = mockMvc.perform(put("/user/protected/" + user_id)
                            .with(jwt().authorities(neededRole))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request))
                    ).andExpect(status().isNotFound())
                    .andReturn();
            String response = mvcResult.getResponse().getContentAsString();
            RestExceptionResponse exceptionResponse = objectMapper.readValue(response, RestExceptionResponse.class);
            assertEquals("USER_NOT_FOUND", exceptionResponse.getError_code());
            assertEquals("No user found with id: " + user_id, exceptionResponse.getError_message());
        }

        @Test
        public void failure_no_permission() throws Exception {
            long user_id = 1;
            UpdateUserRequest updateUserRequest = getMockUpdateUserRequest();
            mockMvc.perform(put("/user/protected/" + user_id)
                            .with(jwt().authorities(notNeededRole))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(updateUserRequest))
                    ).andExpect(status().isForbidden())
                    .andReturn();
        }

        @ParameterizedTest
        @MethodSource("bad_input")
        public void failure_bad_input(long user_id, UpdateUserRequest updateUserRequest) throws Exception {
            mockMvc.perform(put("/user/protected/" + user_id)
                            .with(jwt().authorities(notNeededRole))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(updateUserRequest))
                    ).andExpect(status().isBadRequest())
                    .andReturn();
        }

        public static List<Arguments> bad_input() {
            List<Arguments> list = new ArrayList<>();
            list.add(Arguments.of(-1, getMockUpdateUserRequest()));

            UpdateUserRequest req1 = getMockUpdateUserRequest();
            req1.setPostalCode("postal_code");
            list.add(Arguments.of(1, req1));

            UpdateUserRequest req12 = getMockUpdateUserRequest();
            req12.setPostalCode("-1160");
            list.add(Arguments.of(1, req12));

            UpdateUserRequest req13 = getMockUpdateUserRequest();
            req13.setPostalCode("11b60");
            list.add(Arguments.of(1, req13));

            UpdateUserRequest req14 = getMockUpdateUserRequest();
            req14.setPostalCode("116098");
            list.add(Arguments.of(1, req14));

            UpdateUserRequest req2 = getMockUpdateUserRequest();
            req2.setPhoneNumber("phoneNumber");
            list.add(Arguments.of(1, req2));

            UpdateUserRequest req22 = getMockUpdateUserRequest();
            req22.setPhoneNumber("-012345678");
            list.add(Arguments.of(1, req22));

            UpdateUserRequest req23 = getMockUpdateUserRequest();
            req23.setPhoneNumber("012345678910111213");
            list.add(Arguments.of(1, req23));

            UpdateUserRequest req24 = getMockUpdateUserRequest();
            req24.setPhoneNumber("012345678910111213");
            list.add(Arguments.of(1, req24));

            UpdateUserRequest req25 = getMockUpdateUserRequest();
            req25.setPhoneNumber("1123456789");
            list.add(Arguments.of(1, req25));

            return list;
        }

        public static UpdateUserRequest getMockUpdateUserRequest() {
            return UpdateUserRequest.builder()
                    .firstName("firstName")
                    .lastName("lastName")
                    .street("street")
                    .houseNumber("houseNumber")
                    .city("city")
                    .postalCode("1160")
                    .country("country")
                    .phoneNumber("010234535")
                    .build();
        }
    }

    public User getMockUser() {
        return User.builder()
                .email("email@email.com")
                .username("username")
                .auth0Id("auth0_id")
                .createdAt(Instant.now())
                .city("city")
                .country("country")
                .street("street")
                .houseNumber("houseNumber")
                .firstName("firstName")
                .lastName("lastName")
                .phoneNumber("000000000")
                .postalCode("01055")
                .build();
    }
}