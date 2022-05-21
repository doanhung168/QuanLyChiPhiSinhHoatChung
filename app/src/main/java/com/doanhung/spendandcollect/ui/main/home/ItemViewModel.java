package com.doanhung.spendandcollect.ui.main.home;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.doanhung.spendandcollect.data.model.remote.server_result_model.GetGroupResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.GetItemsResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.GetUserResult;
import com.doanhung.spendandcollect.data.model.remote.model.Group;
import com.doanhung.spendandcollect.data.model.remote.model.Item;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.ServerResult;
import com.doanhung.spendandcollect.data.repository.MainRepository;
import com.doanhung.spendandcollect.util.SingleLiveEvent;
import com.doanhung.spendandcollect.util.event.CreateGroupResultEvent;
import com.doanhung.spendandcollect.util.event.CreateItemResultEvent;
import com.doanhung.spendandcollect.util.event.MessageEvent;
import com.doanhung.spendandcollect.util.event.Event;
import com.doanhung.spendandcollect.util.event.GetUserByIDResultEvent;
import com.doanhung.spendandcollect.util.event.LoadingEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ItemViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    // Phần lưu giá trị qua savedStateHandle
    private final SavedStateHandle savedStateHandle;

    public ItemViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }

    public void setAvatarGroupUri(Uri uri) {
        savedStateHandle.set("avatarGroupURI", uri);
    }

    public Uri getAvatarGroupUri() {
        return savedStateHandle.get("avatarGroupURI");
    }


    public void setItemImageUri(Uri uri) {
        savedStateHandle.set("ItemImage", uri);
    }

    public Uri getItemImageUri() {
        return savedStateHandle.get("ItemImage");
    }


    // phần group hiện tại
    private final MutableLiveData<Group> currentGroup = new MutableLiveData<>();

    public LiveData<Group> getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(Group group) {
        currentGroup.setValue(group);
    }


    // single event
    private final SingleLiveEvent<Event> singleEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<Event> getSingleEvent() {
        return singleEvent;
    }


    // event
    private final MutableLiveData<Event> event = new MutableLiveData<>();
    public LiveData<Event> getEvent() {
        return event;
    }


    // phần group của user
    private final MutableLiveData<List<Group>> groupOfUser = new MutableLiveData<>();

    public LiveData<List<Group>> getGroupOfUser() {
        return groupOfUser;
    }


    // phần item của group
    private final MutableLiveData<List<Item>> items = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Item>> getItems() {
        return items;
    }


    // item hiện tại trên màn hình
    private final MutableLiveData<Item> item = new MutableLiveData<>();

    public LiveData<Item> getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item.setValue(item);
    }


    public void createGroup(MultipartBody.Part groupAvatar, RequestBody groupName, String authToken) {
        singleEvent.setValue(new LoadingEvent(Event.LOADING_EVENT, true));
        MainRepository mainRepository = new MainRepository();
        mainRepository.createGroup(groupAvatar, groupName, authToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ServerResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull ServerResult createGroupResult) {
                        event.setValue(new CreateGroupResultEvent(Event.CREATE_GROUP_RESULT_EVENT, createGroupResult));
                        singleEvent.setValue(new CreateGroupResultEvent(Event.CREATE_GROUP_RESULT_EVENT, createGroupResult));
                        singleEvent.setValue(new LoadingEvent(Event.LOADING_EVENT, false));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, e.getMessage()));
                        singleEvent.setValue(new LoadingEvent(Event.MESSAGE_EVENT, false));
                    }
                });
    }

    public void getGroup(String authToken) {
        MainRepository mainRepository = new MainRepository();
        mainRepository.getGroup(authToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetGroupResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull GetGroupResult getGroupResult) {
                        if (getGroupResult.isSuccess()) {
                            groupOfUser.setValue(getGroupResult.getGroups());
                        } else {
                            singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, getGroupResult.getMessage()));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, e.getMessage()));
                    }
                });
    }

    public void createItem(MultipartBody.Part image, RequestBody content, RequestBody price, RequestBody groupId, String authToken) {
        MainRepository mainRepository = new MainRepository();
        mainRepository.createItem(image, content, price, groupId, authToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ServerResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull ServerResult createItemResult) {
                        singleEvent.setValue(new CreateItemResultEvent(Event.CREATE_ITEM_RESULT_EVENT, createItemResult));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, e.getMessage()));
                    }
                });
    }


    public void getItemOfGroup(String groupId, String authToken) {
        MainRepository mainRepository = new MainRepository();
        mainRepository.getItemOfGroup(groupId, authToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<GetItemsResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull GetItemsResult getItemsResult) {
                        if (getItemsResult.isSuccess()) {
                            items.setValue(getItemsResult.getItems());
                        } else {
                            singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, getItemsResult.getMessage()));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, e.getMessage()));
                    }
                });
    }

//    public void getUserById(String id, String authToken) {
//        MainRepository mainRepository = new MainRepository();
//        mainRepository.getUserById(id, authToken)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<GetUserResult>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        compositeDisposable.add(d);
//                    }
//
//                    @Override
//                    public void onSuccess(@NonNull GetUserResult getUserResult) {
//                        if (getUserResult.isSuccess() && getUserResult.getUser() != null) {
//                            singleEvent.setValue(new GetUserByIDResultEvent(Event.GET_USER_BY_ID_EVENT, getUserResult));
//                        } else {
//                            singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, getUserResult.getMessage()));
//                        }
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        singleEvent.setValue(new MessageEvent(Event.MESSAGE_EVENT, e.getMessage()));
//                    }
//                });
//    }
}
