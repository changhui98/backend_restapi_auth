package com.springboot.jwtlogin.user.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    private String id;
    private String password;

}
