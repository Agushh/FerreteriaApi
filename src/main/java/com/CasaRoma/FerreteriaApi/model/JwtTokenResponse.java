package com.CasaRoma.FerreteriaApi.model;

public class JwtTokenResponse {

    private String token;

    public JwtTokenResponse(String token) {
        this.token = token;
    }

    public JwtTokenResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
