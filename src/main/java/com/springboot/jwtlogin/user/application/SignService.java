package com.springboot.jwtlogin.user.application;

import com.springboot.jwtlogin.user.presentation.dto.request.SignInRequest;
import com.springboot.jwtlogin.user.presentation.dto.response.SignInResponse;
import com.springboot.jwtlogin.user.presentation.dto.request.SignUpRequest;
import com.springboot.jwtlogin.user.presentation.dto.response.SignUpResponse;

public interface SignService {

    SignUpResponse signUp(SignUpRequest signUpRequest);

    SignInResponse signIn(SignInRequest signInRequest) throws RuntimeException;

}
