package com.doanhung.spendandcollect.data.api;

import com.doanhung.spendandcollect.data.model.remote.server_result_model.GetFriendResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.GetGroupResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.GetItemsResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.GetUserResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.SearchUserServerResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.SearchUsersServerResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.ServerResult;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MainApi {

    @Multipart
    @POST("/groups/create")
    Single<ServerResult> createGroup(
            @Part MultipartBody.Part groupAvatar,
            @Part("groupName") RequestBody groupName,
            @Header("Authorization") String authToken
    );

    @FormUrlEncoded
    @PUT("/groups/addMember")
    Observable<ServerResult> addMemberForGroup(
            @Part("members[]") List<String> memberIds
    );


    @GET("/groups/me")
    Single<GetGroupResult> getGroup(
            @Header("Authorization") String authToken
    );

    @Multipart
    @POST("/items/create")
    Single<ServerResult> createItem(
            @Part MultipartBody.Part image,
            @Part("content") RequestBody content,
            @Part("price") RequestBody price,
            @Part("groupId") RequestBody groupId,
            @Header("Authorization") String authToken
    );


    @GET("items/getItemsByGroup/{id}")
    Single<GetItemsResult> getItemOfGroup(
            @Path("id") String id,
            @Header("Authorization") String authToken
    );


    @GET("users/searchUser")
    Observable<SearchUsersServerResult> searchUser(
            @Query("userName") String userName,
            @Header("Authorization") String authToken
    );

    @GET("users/searchUserForAuth")
    Observable<SearchUserServerResult> searchUserForAuth(
            @Header("Authorization") String authToken
    );


    @FormUrlEncoded
    @POST("users/addFriendRequest")
    Single<ServerResult> addFriendRequest(
            @Field("userId") String userId,
            @Header("Authorization") String authToken
    );

    @FormUrlEncoded
    @POST("users/removeFriendRequest")
    Single<ServerResult> removeFriendRequest(
            @Field("userId") String userId,
            @Header("Authorization") String authToken
    );

    @FormUrlEncoded
    @PUT("users/addFriend")
    Observable<ServerResult> addFriend(
            @Field("userId") String userId,
            @Header("Authorization") String authToken
    );

    @FormUrlEncoded
    @PUT("users/removeAddingFriend")
    Observable<ServerResult> removeAddingFriend(
            @Field("userId") String userId,
            @Header("Authorization") String authToken
    );

    @FormUrlEncoded
    @PUT("users/disconnectFriend")
    Observable<ServerResult> disconnectFriend(
            @Field("userId") String userId,
            @Header("Authorization") String authToken
    );

    @GET("users/{id}")
    Observable<GetUserResult> getUserById(
            @Path("id") String id,
            @Header("Authorization") String authToken
    );

    @GET("users/me")
    Observable<GetUserResult> getAuth(
            @Header("Authorization") String authToken
    );

    @GET("friends/{friendId}")
    Observable<GetFriendResult> getFriend(
            @Path("friendId") String friendId,
            @Header("Authorization") String authToken
    );



}
