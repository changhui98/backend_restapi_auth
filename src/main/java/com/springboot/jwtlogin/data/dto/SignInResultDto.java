package com.springboot.jwtlogin.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInResultDto extends SignUpResultDto{

    private String accessToken;
    private String refreshToken;

    @Builder
    public SignInResultDto(boolean success, int code, String msg, String accessToken, String refreshToken) {
        super(success, code, msg);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
