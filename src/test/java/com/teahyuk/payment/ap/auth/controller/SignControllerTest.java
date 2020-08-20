package com.teahyuk.payment.ap.auth.controller;

import com.teahyuk.payment.ap.auth.entity.UserEntity;
import com.teahyuk.payment.ap.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@Transactional
@ActiveProfiles("test")
class SignControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void loginSuccessTest() throws Exception {
        String id = "teahyuk@naver.com";
        String pw = "testPassword";
        userRepository.save(UserEntity.builder()
                        .id(1)
                        .name("teahyuk")
                        .password(passwordEncoder.encode(pw))
                        .uid(id)
                        .build());

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
        userRepository.save(UserEntity.builder()
                        .id(1)
                        .name("teahyuk")
                        .password(passwordEncoder.encode(pw))
                        .uid(id)
                        .build());

        mockMvc.perform(
                post("/v1/signin")
                        .param("id", id)
                        .param("password", "___")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUpSuccessTest() throws Exception {
        String id = "teahyuk@naver.com";
        String pw = "testPassword";
        String name = "teahyuk";

        mockMvc.perform(
                post("/v1/signup")
                        .param("id", id)
                        .param("password", pw)
                        .param("name",name)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        assertThat(userRepository.findByUid(id))
                .isNotEmpty()
                .get()
                .extracting(UserEntity::getName)
                .isEqualTo(name);
    }

    @Test
    void signUpConflictTest() throws Exception {
        String id = "teahyuk@naver.com";
        String pw = "testPassword";
        String name = "teahyuk";
        userRepository.saveAndFlush(UserEntity.builder()
                .id(1)
                .name(name)
                .password(passwordEncoder.encode(pw))
                .uid(id)
                .build());

        mockMvc.perform(
                post("/v1/signup")
                        .param("id", id)
                        .param("password", "___")
                        .param("name",name)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}
