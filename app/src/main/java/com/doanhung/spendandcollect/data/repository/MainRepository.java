package com.doanhung.spendandcollect.data.repository;

import com.doanhung.spendandcollect.data.RetrofitInstance;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.GetFriendResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.GetGroupResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.GetItemsResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.GetUserResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.SearchUserServerResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.ServerResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.SearchUsersServerResult;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainRepository {

    public Single<ServerResult> createGroup(MultipartBody.Part groupAvatar, RequestBody groupName, String authToken) {
        return RetrofitInstance.mainApi.createGroup(groupAvatar, groupName, authToken).subscribeOn(Schedulers.io());
    }

    public Single<GetGroupResult> getGroup(String authToken) {
        return RetrofitInstance.mainApi.getGroup(authToken).subscribeOn(Schedulers.io());
    }

    public Single<ServerResult> createItem(MultipartBody.Part image, RequestBody content, RequestBody price, RequestBody groupId, String authToken) {
        return RetrofitInstance.mainApi.createItem(image, content, price, groupId, authToken).subscribeOn(Schedulers.io());
    }

    public Single<GetItemsResult> getItemOfGroup(String groupId, String authToken) {
        return RetrofitInstance.mainApi.getItemOfGroup(groupId, authToken).subscribeOn(Schedulers.io());
    }

    public Observable<SearchUsersServerResult> searchUser(String userName, String authToken) {
        return  RetrofitInstance.mainApi.searchUser(userName, authToken).subscribeOn(Schedulers.io());
    }

    public Observable<SearchUserServerResult> searchUserForAuth(String authToken) {
        return RetrofitInstance.mainApi.searchUserForAuth(authToken).subscribeOn(Schedulers.io());
    }

    public Single<ServerResult> addFriendRequest(String userId, String authToken) {
        return RetrofitInstance.mainApi.addFriendRequest(userId, authToken).subscribeOn(Schedulers.io());
    }

    public Single<ServerResult> removeFriendRequest(String userId, String authToken) {
        return RetrofitInstance.mainApi.removeFriendRequest(userId, authToken).subscribeOn(Schedulers.io());
    }

    public Observable<GetUserResult> getAuth(String authToken) {
        return RetrofitInstance.mainApi.getAuth(authToken).subscribeOn(Schedulers.io());
    }

    public Observable<GetFriendResult> getFriend(String friendId, String authToken) {
        return RetrofitInstance.mainApi.getFriend(friendId, authToken).subscribeOn(Schedulers.io());
    }

    public Observable<ServerResult> addFriend(String userId, String authToken) {
        return RetrofitInstance.mainApi.addFriend(userId, authToken).subscribeOn(Schedulers.io());
    }
    
    public Observable<ServerResult> removeAddingFriend(String userId, String authToken) {
        return RetrofitInstance.mainApi.removeAddingFriend(userId, authToken).subscribeOn(Schedulers.io());
    }


    public Observable<ServerResult> disconnectFriend(String userId, String authToken) {
        return RetrofitInstance.mainApi.disconnectFriend(userId, authToken).subscribeOn(Schedulers.io());
    }
}
