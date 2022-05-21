package com.doanhung.spendandcollect.util.event;

import com.doanhung.spendandcollect.data.model.remote.server_result_model.SignInResult;

public class SignInResultEvent extends Event{

    private SignInResult signInResult;

    public SignInResultEvent(String name, SignInResult signInResult) {
        super(name);
        this.signInResult = signInResult;
    }

    public SignInResult getSignInResult() {
        return signInResult;
    }

    public void setSignInResult(SignInResult signInResult) {
        this.signInResult = signInResult;
    }
}
