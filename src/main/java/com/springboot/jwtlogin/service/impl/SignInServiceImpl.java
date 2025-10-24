package com.springboot.jwtlogin.service.impl;

import com.springboot.jwtlogin.common.CommonResponse;
import com.springboot.jwtlogin.config.security.JwtTokenProvider;
import com.springboot.jwtlogin.data.User;
import com.springboot.jwtlogin.data.dto.SignInRequest;
import com.springboot.jwtlogin.data.dto.SignInResultDto;
import com.springboot.jwtlogin.data.dto.SignUpRequest;
import com.springboot.jwtlogin.data.dto.SignUpResultDto;
import com.springboot.jwtlogin.data.repository.UserRepository;
import com.springboot.jwtlogin.global.service.TokenService;
import com.springboot.jwtlogin.service.SignService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInServiceImpl implements SignService {

    private final Logger log = LoggerFactory.getLogger(SignInServiceImpl.class);
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    public SignUpResultDto signUp(SignUpRequest signUpRequest) {

        User user;
        if (signUpRequest.getRole().equalsIgnoreCase("admin")) {
            user = User.builder()
                .uid(signUpRequest.getId())
                .name(signUpRequest.getUsername())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .roles(Collections.singletonList("ROLE_ADMIN"))
                .build();
        } else {
            user = User.builder()
                .uid(signUpRequest.getId())
                .name(signUpRequest.getUsername())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        }

        User savedUser = userRepository.save(user);
        SignUpResultDto signUpResultDto = new SignUpResultDto();

        log.info("[getSignUpResult] userEntity 값이 들어왔는지 확인 후 결과값 주입");

        if (!savedUser.getName().isEmpty()) {
            log.info("[getSignUpResult] 정상 처리 완료");
            setSuccessResult(signUpResultDto);
        } else {
            log.info("[getSignUpResult] 실패 처리 완료");
            setFailResult(signUpResultDto);
        }

        return signUpResultDto;
    }

    @Override
    public SignInResultDto signIn(SignInRequest signInRequest) throws RuntimeException {

        log.info("[getSignInResult] signDataHandler 로 회원 정보 요청");
        User user = userRepository.getByUid(signInRequest.getId()).orElseThrow(
            () -> new UsernameNotFoundException("존재하지 않는 회원입니다.")
        );

        log.info("[getSignInResult] 패스워드 비교 수행");
        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException();
        }
        log.info("[getSignInResult] 패스워드 일치");

        log.info("[getSignInResult] SignInResultDto 객체 생성");
        SignInResultDto signInResultDto = SignInResultDto.builder()
            .accessToken(jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles()))
            .refreshToken(jwtTokenProvider.createRefreshToken(user.getUid()))
            .build();

        tokenService.saveRefreshToken(String.valueOf(user.getId()), jwtTokenProvider.createRefreshToken(user.getUid()));

        log.info("[getSignInResult] SignInResultDto 객체에 값 주입");
        setSuccessResult(signInResultDto);

        return signInResultDto;
    }

    private void setSuccessResult(SignUpResultDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFailResult(SignUpResultDto result) {
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }
}
