package com.doanhung.spendandcollect.util.event;

import com.doanhung.spendandcollect.data.model.local.SignUpData;

public class SignUpDataEvent extends Event {

    private SignUpData signUpData;

    public SignUpDataEvent(String name, SignUpData signUpData) {
        super(name);
        this.signUpData = signUpData;
    }

    public SignUpData getSignUpData() {
        return signUpData;
    }

    public void setSignUpData(SignUpData signUpData) {
        this.signUpData = signUpData;
    }
}
