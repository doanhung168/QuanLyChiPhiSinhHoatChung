package com.doanhung.spendandcollect.ui.main.setting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doanhung.spendandcollect.data.model.remote.model.Auth;
import com.doanhung.spendandcollect.data.model.remote.model.Friend;
import com.doanhung.spendandcollect.util.SingleLiveEvent;
import com.doanhung.spendandcollect.util.event.Event;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SettingViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final SingleLiveEvent<Event> singleLiveEvent = new SingleLiveEvent<>();
    public LiveData<Event> getSingleLiveEvent() {
        return singleLiveEvent;
    }

    private final MutableLiveData<Auth> auth = new MutableLiveData<>();
    public LiveData<Auth> getAuth() {
        return auth;
    }

    private final MutableLiveData<Friend> friend = new MutableLiveData<>();
    public LiveData<Friend> getFriend() {
        return friend;
    }



}
