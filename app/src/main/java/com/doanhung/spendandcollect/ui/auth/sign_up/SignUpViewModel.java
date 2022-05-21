package com.doanhung.spendandcollect.ui.auth.sign_up;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doanhung.spendandcollect.data.model.local.SignUpData;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.CheckUserIsExistsResult;
import com.doanhung.spendandcollect.data.repository.AuthRepository;
import com.doanhung.spendandcollect.util.SingleLiveEvent;
import com.doanhung.spendandcollect.util.Util;
import com.doanhung.spendandcollect.util.event.Event;
import com.doanhung.spendandcollect.util.event.SignUpDataEvent;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class SignUpViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final String TAG = "SignUpViewModel";

    public final MutableLiveData<String> userName = new MutableLiveData<>();
    public final MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();
    public final MutableLiveData<String> rePassword = new MutableLiveData<>();

    private final MutableLiveData<String> txtHelper = new MutableLiveData<>();

    public LiveData<String> getTxtHelper() {
        return txtHelper;
    }

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    private final SingleLiveEvent<Event> singleEvent = new SingleLiveEvent<>();

    public LiveData<Event> getSingleEvent() {
        return singleEvent;
    }

    private final MutableLiveData<Event> event = new MutableLiveData<>();

    public LiveData<Event> getEvent() {
        return event;
    }

    public void signUpBtnClick(Activity activity) {
        Util.hideKeyboard(activity);
        txtHelper.setValue("");

        if (checkNullOrEmpty(userName)) {
            txtHelper.setValue("Need userName");
            return;
        }

        if (checkNullOrEmpty(phoneNumber)) {
            txtHelper.setValue("Need phoneNumber");
            return;
        }

        if (checkNullOrEmpty(password)) {
            txtHelper.setValue("Need password");
            return;
        }

        if (checkNullOrEmpty(rePassword)) {
            txtHelper.setValue("Need rePassword");
            return;
        }

        if (!password.getValue().equals(rePassword.getValue())) {
            txtHelper.setValue("Password and RePassWord not match");
            return;
        }

        String phoneNumber = "+84" + this.phoneNumber.getValue();
        if (!Util.isPhoneNumber(phoneNumber)) {
            txtHelper.setValue("Invalid PhoneNumber");
            return;
        }

        isLoading.setValue(true);

        AuthRepository authRepository = new AuthRepository();
        authRepository.checkUserIsExists(userName.getValue(), phoneNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<CheckUserIsExistsResult>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull CheckUserIsExistsResult checkUserIsExistsResult) {
                        if (checkUserIsExistsResult.isSuccess()) {
                            if (checkUserIsExistsResult.isExists()) {
                                isLoading.setValue(false);
                                txtHelper.setValue(checkUserIsExistsResult.getMessage());
                            } else {
                                signWithFirebase(phoneNumber, activity);
                            }
                        } else {
                            isLoading.setValue(false);
                            txtHelper.setValue(checkUserIsExistsResult.getMessage());
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        isLoading.setValue(false);
                        txtHelper.setValue(e.getMessage());
                    }
                });

    }

    private void signWithFirebase(String phoneNumber, Activity activity) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("VI");

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setCallbacks(
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                        Log.d(TAG, "onVerificationCompleted:" + credential);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        Log.w(TAG, "onVerificationFailed", e);
                                        isLoading.setValue(false);

                                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                            txtHelper.setValue("Check phone number again");
                                        } else if (e instanceof FirebaseTooManyRequestsException) {
                                            txtHelper.setValue("Too Many Requests");
                                        } else if (e instanceof FirebaseNetworkException) {
                                            txtHelper.setValue("Checking network");
                                        } else if (e instanceof FirebaseAuthUserCollisionException) {
                                            txtHelper.setValue("AuthUser Collision");
                                        } else if (e instanceof FirebaseApiNotAvailableException) {
                                            txtHelper.setValue("API not Available, Try Again");
                                        } else {
                                            txtHelper.setValue("Try Again");
                                        }

                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String verificationId,
                                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                        Log.d(TAG, "onCodeSent:" + verificationId);
                                        isLoading.setValue(false);
                                        singleEvent.setValue(new SignUpDataEvent(
                                                Event.NAVIGATE_TO_VERIFY_FRAGMENT_EVENT,
                                                new SignUpData(
                                                        verificationId,
                                                        token,
                                                        userName.getValue(),
                                                        phoneNumber,
                                                        password.getValue()
                                                )
                                        ));
                                        event.setValue(new Event(Event.COUNT_DOWN_TIME_EVENT));
                                    }
                                }
                        )
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void signInClick(View view) {
        singleEvent.setValue(new Event(Event.NAVIGATE_TO_SIGN_IN_FRAGMENT_EVENT));
    }

    public boolean checkNullOrEmpty(LiveData<String> stringLiveData) {
        return stringLiveData.getValue() == null || stringLiveData.getValue().trim().isEmpty();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
