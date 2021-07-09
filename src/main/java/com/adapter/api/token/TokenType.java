package com.adapter.api.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenType {

    ACCESSTOKEN("accessToken");

    private String token;

}
