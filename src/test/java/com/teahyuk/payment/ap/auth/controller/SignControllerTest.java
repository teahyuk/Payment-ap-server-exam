package com.teahyuk.payment.ap.auth.controller;

import com.teahyuk.payment.ap.auth.config.security.JwtTokenProvider;
import com.teahyuk.payment.ap.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
@WebMvcTest(SignController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SignControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return charSequence.toString().equals(s);
            }
        };
    }

    @Test
    private void test() {

    }

}