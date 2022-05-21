package com.doanhung.spendandcollect.ui.on_boarding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.doanhung.spendandcollect.data.model.local.OnBoardingItem;

import java.util.List;

public class OnBoardingAdapter extends FragmentStateAdapter {

    public final static String ON_BOARDING_ITEM_KEY = "onBoardingItem";
    private final List<OnBoardingItem> mOnBoardingItemList;


    public OnBoardingAdapter(@NonNull Fragment fragment, List<OnBoardingItem> mOnBoardingItemList) {
        super(fragment);
        this.mOnBoardingItemList = mOnBoardingItemList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new OnBoardingItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(ON_BOARDING_ITEM_KEY, mOnBoardingItemList.get(position));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return mOnBoardingItemList.size();
    }
}
