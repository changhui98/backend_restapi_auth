package com.springboot.jwtlogin.service;

import com.springboot.jwtlogin.data.dto.SignInRequest;
import com.springboot.jwtlogin.data.dto.SignInResultDto;
import com.springboot.jwtlogin.data.dto.SignUpRequest;
import com.springboot.jwtlogin.data.dto.SignUpResultDto;

public interface SignService {

    SignUpResultDto signUp(SignUpRequest signUpRequest);

    SignInResultDto signIn(SignInRequest signInRequest) throws RuntimeException;

}
