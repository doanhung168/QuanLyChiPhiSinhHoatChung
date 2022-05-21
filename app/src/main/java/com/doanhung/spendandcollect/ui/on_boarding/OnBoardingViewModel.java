package com.doanhung.spendandcollect.ui.on_boarding;

import android.app.Application;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.doanhung.spendandcollect.R;
import com.doanhung.spendandcollect.data.model.local.OnBoardingItem;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingViewModel extends ViewModel {

    private final Application mApplication;
    public OnBoardingViewModel(Application application) {
        mApplication = application;
    }

    public OnBoardingAdapter createOnBoardingAdapter(Fragment fragment) {
        return new OnBoardingAdapter(fragment, getOnBoardingItemList());
    }

    public List<OnBoardingItem> getOnBoardingItemList() {
        List<OnBoardingItem> onBoardingItemList = new ArrayList<>();
        onBoardingItemList.add(new OnBoardingItem(
                mApplication.getString(R.string.first_slide_title),
                mApplication.getString(R.string.first_slide_desc),
                R.drawable.ic_launcher_foreground
        ));
        onBoardingItemList.add(new OnBoardingItem(
                mApplication.getString(R.string.second_slide_title),
                mApplication.getString(R.string.second_slide_desc),
                R.drawable.ic_launcher_foreground
        ));
        onBoardingItemList.add(new OnBoardingItem(
                mApplication.getString(R.string.third_slide_title),
                mApplication.getString(R.string.third_slide_desc),
                R.drawable.ic_launcher_foreground
        ));
        onBoardingItemList.add(new OnBoardingItem(
                mApplication.getString(R.string.fourth_slide_title),
                mApplication.getString(R.string.fourth_slide_desc),
                R.drawable.ic_launcher_foreground

        ));
        return onBoardingItemList;
    }
}
