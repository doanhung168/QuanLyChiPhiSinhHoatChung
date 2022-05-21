package com.doanhung.spendandcollect.data.model.remote.server_result_model;

public class SignInResult extends ServerResult {

    private String token;

    public SignInResult(boolean success, String message, String token) {
        super(success, message);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
