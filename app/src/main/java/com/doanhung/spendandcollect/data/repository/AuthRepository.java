package com.doanhung.spendandcollect.data.repository;

import com.doanhung.spendandcollect.data.RetrofitInstance;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.CheckUserIsExistsResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.ServerResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.SignInResult;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AuthRepository {

    public Single<ServerResult> signUp(String userName, String password, String phoneNumber) {
        return RetrofitInstance.authApi.signUp(userName, password, phoneNumber).subscribeOn(Schedulers.io());
    }

    public Single<CheckUserIsExistsResult> checkUserIsExists(String userName, String phoneNumber) {
        return RetrofitInstance.authApi.checkUserIsExists(userName, phoneNumber).subscribeOn(Schedulers.io());
    }

    public Single<SignInResult> signIn(String userName, String password) {
        return RetrofitInstance.authApi.signIn(userName, password).subscribeOn(Schedulers.io());
    }

    public Single<SignInResult> signInWithFacebook(String authFacebookID, String userName, String image) {
        return RetrofitInstance.authApi.signInWithFB(authFacebookID, userName, image).subscribeOn(Schedulers.io());
    }

    public Single<SignInResult> signInWithGoogle(String authGoogleID, String userName, String image) {
        return RetrofitInstance.authApi.signInWithGoogle(authGoogleID, userName, image).subscribeOn(Schedulers.io());
    }



}
