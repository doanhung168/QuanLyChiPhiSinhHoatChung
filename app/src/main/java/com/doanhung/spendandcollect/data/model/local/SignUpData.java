package com.doanhung.spendandcollect.data.model.local;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.PhoneAuthProvider;

public class SignUpData implements Parcelable {

    private String verificationID;
    private PhoneAuthProvider.ForceResendingToken token;
    private String userName;
    private String phoneNumber;
    private String password;


    protected SignUpData(Parcel in) {
        verificationID = in.readString();
        token = in.readParcelable(PhoneAuthProvider.ForceResendingToken.class.getClassLoader());
        userName = in.readString();
        phoneNumber = in.readString();
        password = in.readString();
    }

    public SignUpData(String verificationID, PhoneAuthProvider.ForceResendingToken token, String userName, String phoneNumber, String password) {
        this.verificationID = verificationID;
        this.token = token;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getVerificationID() {
        return verificationID;
    }

    public void setVerificationID(String verificationID) {
        this.verificationID = verificationID;
    }

    public PhoneAuthProvider.ForceResendingToken getToken() {
        return token;
    }

    public void setToken(PhoneAuthProvider.ForceResendingToken token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static final Creator<SignUpData> CREATOR = new Creator<SignUpData>() {
        @Override
        public SignUpData createFromParcel(Parcel in) {
            return new SignUpData(in);
        }

        @Override
        public SignUpData[] newArray(int size) {
            return new SignUpData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(verificationID);
        parcel.writeParcelable(token, i);
        parcel.writeString(userName);
        parcel.writeString(phoneNumber);
        parcel.writeString(password);
    }
}
