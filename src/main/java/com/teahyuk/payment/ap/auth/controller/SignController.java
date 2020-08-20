package com.teahyuk.payment.ap.auth.controller;

import com.teahyuk.payment.ap.auth.config.security.JwtTokenProvider;
import com.teahyuk.payment.ap.auth.entity.UserEntity;
import com.teahyuk.payment.ap.auth.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Api(tags = "1. Sign")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
    @PostMapping(value = "/signin")
    public String signIn(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String id,
                         @ApiParam(value = "비밀번호", required = true) @RequestParam String password) throws SignInFailedException {
        UserEntity user = userRepository.findByUid(id).orElseThrow(() -> new SignInFailedException("not found id"));
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new SignInFailedException("password error");
        return jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles());

    }

    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public ResponseEntity<Boolean> signUp(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String id,
                                          @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
                                          @ApiParam(value = "이름", required = true) @RequestParam String name) {

        userRepository.save(UserEntity.builder()
                .uid(id)
                .password(passwordEncoder.encode(password))
                .name(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        return ResponseEntity.ok(true);
    }
}
