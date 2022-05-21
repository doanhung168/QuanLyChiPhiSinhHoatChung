package com.doanhung.spendandcollect.util.event;

import com.doanhung.spendandcollect.data.model.remote.server_result_model.ServerResult;

public class SignUpResultEvent extends Event{
    private ServerResult signUpServerResult;

    public SignUpResultEvent(String name, ServerResult signUpServerResult) {
        super(name);
        this.signUpServerResult = signUpServerResult;
    }

    public ServerResult getSignUpServerResult() {
        return signUpServerResult;
    }

    public void setSignUpServerResult(ServerResult signUpServerResult) {
        this.signUpServerResult = signUpServerResult;
    }
}
