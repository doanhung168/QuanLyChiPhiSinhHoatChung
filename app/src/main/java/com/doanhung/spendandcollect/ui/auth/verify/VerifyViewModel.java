package com.doanhung.spendandcollect.ui.auth.verify;

import static com.doanhung.spendandcollect.util.event.Event.COUNT_DOWN_TIME_EVENT;
import static com.doanhung.spendandcollect.util.event.Event.SIGN_UP_RESULT_EVENT;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.doanhung.spendandcollect.data.model.local.SignUpData;
import com.doanhung.spendandcollect.data.model.remote.server_result_model.ServerResult;
import com.doanhung.spendandcollect.data.repository.AuthRepository;
import com.doanhung.spendandcollect.util.SingleLiveEvent;
import com.doanhung.spendandcollect.util.Util;
import com.doanhung.spendandcollect.util.event.Event;
import com.doanhung.spendandcollect.util.event.SignUpResultEvent;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class VerifyViewModel extends ViewModel {

    private static final String TAG = "VerifyViewModel";

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public final MutableLiveData<String> txtHelper = new MutableLiveData<>();

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    private final SingleLiveEvent<Event> singleEvent = new SingleLiveEvent<>();

    public LiveData<Event> getSingleEvent() {
        return singleEvent;
    }

    public final MutableLiveData<String> verificationCode1 = new MutableLiveData<>("");
    public final MutableLiveData<String> verificationCode2 = new MutableLiveData<>("");
    public final MutableLiveData<String> verificationCode3 = new MutableLiveData<>("");
    public final MutableLiveData<String> verificationCode4 = new MutableLiveData<>("");
    public final MutableLiveData<String> verificationCode5 = new MutableLiveData<>("");
    public final MutableLiveData<String> verificationCode6 = new MutableLiveData<>("");


    private SignUpData signUpData;

    public void setSignUpData(SignUpData signUpData) {
        this.signUpData = signUpData;
    }

    public SignUpData getSignUpData() {
        return signUpData;
    }

    public void resetEditText() {
        verificationCode6.setValue("");
        verificationCode5.setValue("");
        verificationCode4.setValue("");
        verificationCode3.setValue("");
        verificationCode2.setValue("");
        verificationCode1.setValue("");
    }

    public boolean acceptToVerify() {
        return verificationCode1.getValue().length() == 1
                && verificationCode2.getValue().length() == 1
                && verificationCode3.getValue().length() == 1
                && verificationCode4.getValue().length() == 1
                && verificationCode5.getValue().length() == 1
                && verificationCode6.getValue().length() == 1;
    }

    public void verify(Activity activity) {
        Util.hideKeyboard(activity);
        isLoading.setValue(true);
        txtHelper.setValue("");

        String code = verificationCode1.getValue() + verificationCode2.getValue() + verificationCode3.getValue()
                + verificationCode4.getValue() + verificationCode5.getValue() + verificationCode6.getValue();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(signUpData.getVerificationID(), code);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        signUp(user);

                    } else {
                        resetEditText();
                        isLoading.setValue(false);
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            txtHelper.setValue("Auth Invalid Credentials");
                        } else if (task.getException() instanceof FirebaseNetworkException) {
                            txtHelper.setValue("Check network again!");
                        } else if (task.getException() instanceof FirebaseTooManyRequestsException) {
                            txtHelper.setValue("Too Many Requests!");
                        } else {
                            txtHelper.setValue("Error, Try again!");
                        }
                    }
                });
    }

    private void signUp(FirebaseUser user) {
        AuthRepository authRepository = new AuthRepository();
        authRepository.signUp(signUpData.getUserName(), signUpData.getPassword(), user.getPhoneNumber())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ServerResult>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ServerResult signUpResult) {
                        resetEditText();
                        isLoading.setValue(false);
                        singleEvent.setValue(new SignUpResultEvent(SIGN_UP_RESULT_EVENT, signUpResult));
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        resetEditText();
                        isLoading.setValue(false);
                        txtHelper.setValue(e.getMessage());
                    }
                });

    }


    public void reSendOTP(View view, Activity activity) {
        txtHelper.setValue("");
        resetEditText();
        isLoading.setValue(true);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("VI");
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(signUpData.getPhoneNumber())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setForceResendingToken(signUpData.getToken())
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
                                            txtHelper.setValue("Error, Try Again");
                                        }

                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String verificationId,
                                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                        Log.d(TAG, "onCodeSent:" + verificationId);
                                        isLoading.setValue(false);
                                        signUpData = new SignUpData(
                                                verificationId,
                                                token,
                                                signUpData.getUserName(),
                                                signUpData.getPhoneNumber(),
                                                signUpData.getPassword()
                                        );
                                        singleEvent.setValue(new Event(COUNT_DOWN_TIME_EVENT));
                                    }
                                }
                        )
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
