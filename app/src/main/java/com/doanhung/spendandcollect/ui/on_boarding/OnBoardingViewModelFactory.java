package com.doanhung.spendandcollect.ui.on_boarding;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.doanhung.spendandcollect.ui.on_boarding.OnBoardingViewModel;

public class OnBoardingViewModelFactory implements ViewModelProvider.Factory {

    private final Application mApplication;
    public OnBoardingViewModelFactory(Application application) {
        mApplication = application;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(OnBoardingViewModel.class)) {
            return (T) new OnBoardingViewModel(mApplication);
        }
        throw new IllegalArgumentException("Unable construct viewModel");
    }
}
