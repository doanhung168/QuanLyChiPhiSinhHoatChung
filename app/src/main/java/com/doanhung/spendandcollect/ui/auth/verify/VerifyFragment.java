package com.doanhung.spendandcollect.ui.auth.verify;

import static com.doanhung.spendandcollect.util.event.Event.COUNT_DOWN_TIME_EVENT;
import static com.doanhung.spendandcollect.util.event.Event.SIGN_UP_RESULT_EVENT;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.doanhung.spendandcollect.R;
import com.doanhung.spendandcollect.data.model.local.SignUpData;
import com.doanhung.spendandcollect.databinding.FragmentVerifyBinding;
import com.doanhung.spendandcollect.ui.auth.sign_up.SignUpViewModel;
import com.doanhung.spendandcollect.util.event.SignUpResultEvent;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VerifyFragment extends Fragment {

    private static final String TAG = "EnterOPTFragment";

    private FragmentVerifyBinding fragmentEnterOptBinding;

    private VerifyViewModel verifyViewModel;
    private SignUpViewModel signUpViewModel;

    private NavController navController;
    private CountDownTimer countDownTimer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
        navController = NavHostFragment.findNavController(this);
        verifyViewModel = new ViewModelProvider(this).get(VerifyViewModel.class);
        signUpViewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);

        // data is a object of signUpData include : verifyCode, userName, password, phoneNumber
        getDataFromSignUpFragmentPassToViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        fragmentEnterOptBinding = FragmentVerifyBinding.inflate(inflater, container, false);
        return fragmentEnterOptBinding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: ");

        fragmentEnterOptBinding.setLifecycleOwner(getViewLifecycleOwner());
        fragmentEnterOptBinding.setVerifyViewModel(verifyViewModel);
        fragmentEnterOptBinding.setActivity(requireActivity());


        // observe value of user input
        observeLiveData(verifyViewModel.verificationCode1);
        observeLiveData(verifyViewModel.verificationCode2);
        observeLiveData(verifyViewModel.verificationCode3);
        observeLiveData(verifyViewModel.verificationCode4);
        observeLiveData(verifyViewModel.verificationCode5);
        observeLiveData(verifyViewModel.verificationCode6);

        signUpViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event.getName().equals(COUNT_DOWN_TIME_EVENT)) {
                setUpTimeForResendOTP();
            }
        });

        verifyViewModel.getSingleEvent().observe(getViewLifecycleOwner(), event -> {
            if (event.getName().equals(COUNT_DOWN_TIME_EVENT)) {
                setUpTimeForResendOTP();
            } else if (event.getName().equals(SIGN_UP_RESULT_EVENT)) {
                if (((SignUpResultEvent) event).getSignUpServerResult().isSuccess()) {
                    navController.navigate(R.id.action_verifyFragment_to_signInFragment);
                }
                Toast.makeText(requireContext(), ((SignUpResultEvent) event).getSignUpServerResult().getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    // setup textview resent otp wait 60s
    private void setUpTimeForResendOTP() {
        fragmentEnterOptBinding.tvSentTo.setText(verifyViewModel.getSignUpData().getPhoneNumber());
        fragmentEnterOptBinding.tvResentOPT.setEnabled(false);
        fragmentEnterOptBinding.tvResentOPT.setText("Didn't receive the OTP?\n Waiting 60s to send request OTP");
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                String duration = String.format(
                        Locale.ROOT,
                        "Didn't receive the OTP?\n Waiting %02d:%02d s to send request OTP",
                        TimeUnit.MILLISECONDS.toMinutes(l),
                        TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))); // 59000 = 59 -0
                fragmentEnterOptBinding.tvResentOPT.setText(duration);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                fragmentEnterOptBinding.tvResentOPT.setText("ReSend OTP");
                fragmentEnterOptBinding.tvResentOPT.setEnabled(true);
            }
        }.start();

    }

    @Override
    public void onStop() {
        Log.e(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        countDownTimer.cancel();
        super.onDestroyView();
        fragmentEnterOptBinding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }

    private void getDataFromSignUpFragmentPassToViewModel() {
        SignUpData signUpData =
                VerifyFragmentArgs.fromBundle(getArguments()).getSignUpData();
        verifyViewModel.setSignUpData(signUpData);
    }

    private void observeLiveData(LiveData<String> liveData) {
        liveData.observe(getViewLifecycleOwner(), s -> {
            if (verifyViewModel.acceptToVerify()) {
                verifyViewModel.verify(requireActivity());
            }
        });
    }


}