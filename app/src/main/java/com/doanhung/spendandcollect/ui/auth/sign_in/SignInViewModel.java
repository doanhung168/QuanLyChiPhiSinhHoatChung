package com.doanhung.spendandcollect.ui.auth.sign_in;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doanhung.spendandcollect.data.model.remote.model.User;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.GetUserResult;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.SignInResult;
import com.doanhung.spendandcollect.data.repository.AuthRepository;
import com.doanhung.spendandcollect.data.repository.MainRepository;
import com.doanhung.spendandcollect.util.SingleLiveEvent;
import com.doanhung.spendandcollect.util.Util;
import com.doanhung.spendandcollect.util.event.Event;
import com.doanhung.spendandcollect.util.event.FailAuthEvent;
import com.doanhung.spendandcollect.util.event.LoadingEvent;
import com.doanhung.spendandcollect.util.event.MessageEvent;
import com.doanhung.spendandcollect.util.event.SignInResultEvent;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;


public class SignInViewModel extends ViewModel {


    private static final String TAG = "SignInViewModel";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public final MutableLiveData<String> userName = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();

    private final MutableLiveData<String> txtHelper = new MutableLiveData<>();

    public LiveData<String> getTxtHelper() {
        return txtHelper;
    }

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    private final SingleLiveEvent<Event> singleEvent = new SingleLiveEvent<>();

    public LiveData<Event> getEvent() {
        return singleEvent;
    }

    private final MutableLiveData<User> auth = new MutableLiveData<>();
    public LiveData<User> getAuth() {
        return auth;
    }

    public void signUpClick(View view) {
        singleEvent.setValue(new Event(Event.NAVIGATE_TO_SIGN_UP_FRAGMENT_EVENT));
    }

    public void signIn(View view, Activity activity) {
        Util.hideKeyboard(activity);
        txtHelper.setValue("");

        if (checkNullOrEmpty(userName)) {
            {
                txtHelper.setValue("Check userName");
                return;
            }
        }

        if (checkNullOrEmpty(password)) {
            txtHelper.setValue("Check password");
            return;
        }

        isLoading.setValue(true);
        AuthRepository authRepository = new AuthRepository();
        authRepository.signIn(userName.getValue(), password.getValue())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SignInResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull SignInResult signInResult) {
                        if (signInResult.isSuccess()) {
                            isLoading.setValue(false);
                            singleEvent.setValue(new SignInResultEvent(Event.SIGN_IN_RESULT_EVENT, signInResult));
                        } else {
                            isLoading.setValue(false);
                            txtHelper.setValue(signInResult.getMessage());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoading.setValue(false);
                        txtHelper.setValue(e.getMessage());
                    }
                });
    }

    public void signInWithFacebook(String authFacebookID, String userName, String image) {
        isLoading.setValue(true);
        txtHelper.setValue("");

        AuthRepository authRepository = new AuthRepository();
        authRepository.signInWithFacebook(authFacebookID, userName, image)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SignInResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull SignInResult signInResult) {
                        if (signInResult.isSuccess()) {
                            isLoading.setValue(false);
                            singleEvent.setValue(new SignInResultEvent(Event.SIGN_IN_RESULT_EVENT, signInResult));
                        } else {
                            isLoading.setValue(false);
                            txtHelper.setValue(signInResult.getMessage());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoading.setValue(false);
                        txtHelper.setValue(e.getMessage());
                    }
                });
    }

    public void signInWithGoogle(String authGoogleID, String userName, String image) {
        isLoading.setValue(true);
        txtHelper.setValue("");

        AuthRepository authRepository = new AuthRepository();
        authRepository.signInWithGoogle(authGoogleID, userName, image)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SignInResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull SignInResult signInResult) {
                        if (signInResult.isSuccess()) {
                            isLoading.setValue(false);
                            singleEvent.setValue(new SignInResultEvent(Event.SIGN_IN_RESULT_EVENT, signInResult));
                        } else {
                            isLoading.setValue(false);
                            txtHelper.setValue(signInResult.getMessage());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoading.setValue(false);
                        txtHelper.setValue(e.getMessage());
                    }
                });
    }

    public void getAuth(String authToken) {
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
                            singleEvent.setValue(new FailAuthEvent(Event.FAIL_AUTH_EVENT, getUserResult.getMessage()));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        singleEvent.setValue(new FailAuthEvent(Event.FAIL_AUTH_EVENT, e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: get Auth for sign In");
                    }
                });
    }

    public boolean checkNullOrEmpty(LiveData<String> stringLiveData) {
        return stringLiveData.getValue() == null || stringLiveData.getValue().trim().isEmpty();
    }


    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
