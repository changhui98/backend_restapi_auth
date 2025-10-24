package com.springboot.jwtlogin.user.presentation;

import com.springboot.jwtlogin.user.presentation.dto.request.SignInRequest;
import com.springboot.jwtlogin.user.presentation.dto.response.SignInResponse;
import com.springboot.jwtlogin.user.presentation.dto.request.SignUpRequest;
import com.springboot.jwtlogin.user.presentation.dto.response.SignUpResponse;
import com.springboot.jwtlogin.user.application.SignService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign-api")
@RequiredArgsConstructor
public class SignController {

    private final Logger log = LoggerFactory.getLogger(SignController.class);
    private final SignService signService;

    @PostMapping("/sign-in")
    public SignInResponse signIn(
        @RequestBody SignInRequest signInRequest
    ) throws RuntimeException {


        SignInResponse signInResponse = signService.signIn(signInRequest);

        return signInResponse;
    }

    @PostMapping("/sign-up")
    public SignUpResponse signUp(
        @RequestBody SignUpRequest request
    ) {

        SignUpResponse saved = signService.signUp(request);

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
