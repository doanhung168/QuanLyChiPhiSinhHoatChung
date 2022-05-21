package com.doanhung.spendandcollect.data.api;

import com.doanhung.spendandcollect.data.model.remote.server_result_model.CheckUserIsExistsResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.ServerResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.SignInResult;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApi {

    @FormUrlEncoded
    @POST("/users/signUp")
    Single<ServerResult> signUp(
            @Field("userName") String userName,
            @Field("password") String password,
            @Field("phoneNumber") String phoneNumber
    );

    @FormUrlEncoded
    @POST("/users/checkUserIsExists")
    Single<CheckUserIsExistsResult> checkUserIsExists(
            @Field("userName") String userName,
            @Field("phoneNumber") String phoneNumber
    );

    @FormUrlEncoded
    @POST("/users/signIn")
    Single<SignInResult> signIn(
            @Field("userName") String userName,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/users/signInWithFacebook")
    Single<SignInResult> signInWithFB(
            @Field("authFacebookID") String authFacebookID,
            @Field("userName") String userName,
            @Field("image") String image
    );

    @FormUrlEncoded
    @POST("/users/signInWithGoogle")
    Single<SignInResult> signInWithGoogle(
            @Field("authGoogleID") String authGoogleID,
            @Field("userName") String userName,
            @Field("image") String image
    );


}
