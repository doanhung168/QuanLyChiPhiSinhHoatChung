package com.doanhung.spendandcollect.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doanhung.spendandcollect.data.model.remote.model.Friend;
import com.doanhung.spendandcollect.data.model.remote.model.User;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.GetFriendResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.GetUserResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.ServerResult;
import com.doanhung.spendandcollect.data.repository.MainRepository;
import com.doanhung.spendandcollect.util.SingleLiveEvent;
import com.doanhung.spendandcollect.util.event.Event;
import com.doanhung.spendandcollect.util.event.LoadingEvent;
import com.doanhung.spendandcollect.util.event.MessageEvent;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<User> auth = new MutableLiveData<>();

    public LiveData<User> getAuth() {
        return auth;
    }

    private final MutableLiveData<Friend> friend = new MutableLiveData<>();

    public LiveData<Friend> getFriend() {
        return friend;
    }

    private final SingleLiveEvent<Event> singleEvent = new SingleLiveEvent<>();

    public LiveData<Event> getSingleEvent() {
        return singleEvent;
    }

    public void getAuth(String authToken) {
        singleEvent.setValue(new LoadingEvent(Event.LOADING_EVENT, true));
        MainRepository mainRepository = new MainRepository();
        mainRepository.getAuth(authToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetUserResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull GetUserResult getUserResult) {
                        if (getUserResult.isSuccess()) {
                            auth.setValue(getUserResult.getUser());
                        } else {
                            singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, getUserResult.getMessage()));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        singleEvent.setValue(new LoadingEvent(Event.LOADING_EVENT, false));
                        Log.e(TAG, "onComplete: get Auth");
                    }
                });
    }

    public void getFriend(String authToken) {
        singleEvent.setValue(new LoadingEvent(Event.LOADING_EVENT, true));
        MainRepository mainRepository = new MainRepository();
        mainRepository.getAuth(authToken)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<GetUserResult, ObservableSource<GetFriendResult>>() {
                    @Override
                    public ObservableSource<GetFriendResult> apply(GetUserResult getUserResult) {
                        if(getUserResult.isSuccess()) {
                            auth.setValue(getUserResult.getUser());
                        } else {
                            singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, getUserResult.getMessage()));
                        }
                        return mainRepository.getFriend(auth.getValue().getFriend(), authToken).subscribeOn(Schedulers.io());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetFriendResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull GetFriendResult getFriendResult) {
                        if (getFriendResult.isSuccess()) {
                            friend.setValue(getFriendResult.getFriend());
                        } else {
                            singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, getFriendResult.getMessage()));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        singleEvent.setValue(new LoadingEvent(Event.LOADING_EVENT, false));
                        Log.e(TAG, "onComplete: get friend");
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
                        if(serverResult.isSuccess()) {
                            singleEvent.setValue(new Event(Event.REFRESH_DATA_EVENT));
                        } else {
                            singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, serverResult.getMessage()));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: add friend");
                    }
                });
    }

    public void removeAddingFriend(String userId, String token) {
        MainRepository mainRepository = new MainRepository();
        mainRepository.removeAddingFriend(userId, token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull ServerResult serverResult) {
                        if(serverResult.isSuccess()) {
                            singleEvent.setValue(new Event(Event.REFRESH_DATA_EVENT));
                        } else {
                            singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, serverResult.getMessage()));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: remove adding friend" );
                    }
                });
    }


    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }



}
