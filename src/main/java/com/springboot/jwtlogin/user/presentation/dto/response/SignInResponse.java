package com.springboot.jwtlogin.user.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInResponse extends SignUpResponse {

    private String accessToken;
    private String refreshToken;

    @Builder
    public SignInResponse(boolean success, int code, String msg, String accessToken, String refreshToken) {
        super(success, code, msg);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
