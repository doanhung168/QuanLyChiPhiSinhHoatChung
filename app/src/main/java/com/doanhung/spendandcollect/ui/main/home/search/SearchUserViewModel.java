package com.doanhung.spendandcollect.ui.main.home.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doanhung.spendandcollect.data.model.remote.model.SearchUser;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.SearchUserServerResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.SearchUsersServerResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.ServerResult;
import com.doanhung.spendandcollect.data.repository.MainRepository;
import com.doanhung.spendandcollect.util.SingleLiveEvent;
import com.doanhung.spendandcollect.util.event.Event;
import com.doanhung.spendandcollect.util.event.MessageEvent;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchUserViewModel extends ViewModel {

    private static final String TAG = "SearchUserViewModel";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<List<SearchUser>> users = new MutableLiveData<>();

    public MutableLiveData<List<SearchUser>> getUsers() {
        return users;
    }

    private final MutableLiveData<SearchUser> auth = new MutableLiveData<>();

    public LiveData<SearchUser> getAuth() {
        return auth;
    }

    private final SingleLiveEvent<Event> singleLiveEvent = new SingleLiveEvent<>();

    public LiveData<Event> getSingleLiveEvent() {
        return singleLiveEvent;
    }

    public void searchUser(String userName, String authToken) {
        MainRepository mainRepository = new MainRepository();
        mainRepository.searchUserForAuth(authToken)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<SearchUserServerResult, ObservableSource<SearchUsersServerResult>>) searchUserServerResult -> {
                    if (searchUserServerResult.isSuccess()) {
                        auth.setValue(searchUserServerResult.getAuth());
                    } else {
                        singleLiveEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, searchUserServerResult.getMessage()));
                    }
                    return mainRepository.searchUser(userName, authToken).subscribeOn(Schedulers.io());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchUsersServerResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull SearchUsersServerResult searchUsersServerResult) {
                        if (searchUsersServerResult.isSuccess()) {
                            users.setValue(searchUsersServerResult.getUsers());
                        } else {
                            singleLiveEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, searchUsersServerResult.getMessage()));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        singleLiveEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: search user");
                    }
                });
    }

    public void addFriendRequest(String userId, String authToken) {
        MainRepository mainRepository = new MainRepository();
        mainRepository.addFriendRequest(userId, authToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ServerResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull ServerResult serverResult) {
                        if (serverResult.isSuccess()) {
                            singleLiveEvent.setValue(new Event(Event.LOADING_STATE_FRIEND));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        singleLiveEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, e.getMessage()));
                    }
                });
    }

    public void removeFriendRequest(String userId, String authToken) {
        MainRepository mainRepository = new MainRepository();
        mainRepository.removeFriendRequest(userId, authToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ServerResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull ServerResult serverResult) {
                        if (serverResult.isSuccess()) {
                            singleLiveEvent.setValue(new Event(Event.LOADING_STATE_FRIEND));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        singleLiveEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, e.getMessage()));
                    }
                });
    }

    public void addFriend(String userId, String authToken) {
        MainRepository mainRepository = new MainRepository();
        mainRepository.addFriend(userId, authToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull ServerResult serverResult) {
                        if (serverResult.isSuccess()) {
                            singleLiveEvent.setValue(new Event(Event.LOADING_STATE_FRIEND));
                        } else {
                            singleLiveEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, serverResult.getMessage()));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        singleLiveEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: add friend");
                    }
                });
    }

    public void disConnectFriend(String userId, String authToken) {
        MainRepository mainRepository = new MainRepository();
        mainRepository.disconnectFriend(userId, authToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull ServerResult serverResult) {
                        if (serverResult.isSuccess()) {
                            singleLiveEvent.setValue(new Event(Event.LOADING_STATE_FRIEND));
                        } else {
                            singleLiveEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, serverResult.getMessage()));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        singleLiveEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: disconnect friend");
                    }
                });

    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
