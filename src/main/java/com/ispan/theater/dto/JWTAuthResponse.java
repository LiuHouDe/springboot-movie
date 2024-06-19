package com.ispan.theater.dto;

public class JWTAuthResponse {
    private String token;
    private String tokenType = "Bearer ";
    public JWTAuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        return "JWTAuthResponse [" +
                "token='" + token +
                ", tokenType='" + tokenType +
                ']';
    }
}
