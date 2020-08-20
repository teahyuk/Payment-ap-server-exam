package com.teahyuk.payment.ap.auth.controller;

import com.teahyuk.payment.ap.auth.config.security.JwtTokenProvider;
import com.teahyuk.payment.ap.auth.entity.UserEntity;
import com.teahyuk.payment.ap.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = SignController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ActiveProfiles("test")
class SignControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setting(){
        given(passwordEncoder.matches(anyString(),anyString()))
                .willAnswer(v->{
                    String password = v.getArgument(0);
                    String encoded = v.getArgument(1);
                    return password.equals(encoded);
                });
    }

    @Test
    void loginSuccessTest() throws Exception {
        String id = "teahyuk@naver.com";
        String pw = "testPassword";
        given(userRepository.findByUid(id))
                .willReturn(Optional.of(UserEntity.builder()
                        .id(1)
                        .name("teahyuk")
                        .password(pw)
                        .uid(id)
                        .build()));

        mockMvc.perform(
                post("/v1/signin")
                        .param("id", id)
                        .param("password", pw)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void notFoundIdTest() throws Exception {
        String id = "teahyuk@naver.com";
        String pw = "testPassword";
        given(userRepository.findByUid(id))
                .willReturn(Optional.empty());

        mockMvc.perform(
                post("/v1/signin")
                        .param("id", id)
                        .param("password", pw)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidPWTest() throws Exception {
        String id = "teahyuk@naver.com";
        String pw = "testPassword";
        given(userRepository.findByUid(id))
                .willReturn(Optional.of(UserEntity.builder()
                        .id(1)
                        .name("teahyuk")
                        .password(pw)
                        .uid(id)
                        .build()));

        mockMvc.perform(
                post("/v1/signin")
                        .param("id", id)
                        .param("password", "invalid")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
