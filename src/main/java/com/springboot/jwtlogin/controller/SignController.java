package com.springboot.jwtlogin.controller;

import com.springboot.jwtlogin.data.dto.SignInRequest;
import com.springboot.jwtlogin.data.dto.SignInResultDto;
import com.springboot.jwtlogin.data.dto.SignUpRequest;
import com.springboot.jwtlogin.data.dto.SignUpResultDto;
import com.springboot.jwtlogin.service.SignService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign-api")
@RequiredArgsConstructor
public class SignController {

    private final Logger log = LoggerFactory.getLogger(SignController.class);
    private final SignService signService;

    @PostMapping("/sign-in")
    public SignInResultDto signIn(
        @RequestBody SignInRequest signInRequest
    ) throws RuntimeException {


        SignInResultDto signInResultDto = signService.signIn(signInRequest);

        return signInResultDto;
    }

    @PostMapping("/sign-up")
    public SignUpResultDto signUp(
        @RequestBody SignUpRequest request
    ) {

        SignUpResultDto saved = signService.signUp(request);

        return saved;
    }

    @GetMapping("/exception")
    public void exceptionTest() throws RuntimeException {
        throw new RuntimeException("접근이 금지되었습니다.");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> ExceptionHandler(RuntimeException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.info("ExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "에러 발생");

        return new ResponseEntity<>(map, responseHeaders, httpStatus);
    }

}
