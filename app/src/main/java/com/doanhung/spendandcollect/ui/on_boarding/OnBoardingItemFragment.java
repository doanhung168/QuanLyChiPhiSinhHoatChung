package com.doanhung.spendandcollect.ui.on_boarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.doanhung.spendandcollect.R;
import com.doanhung.spendandcollect.data.model.local.OnBoardingItem;
import com.doanhung.spendandcollect.databinding.FragmentOnBoardingItemBinding;

public class OnBoardingItemFragment extends Fragment {

    private static final String TAG = "OnBoardingItemFragment";
    private FragmentOnBoardingItemBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_on_boarding_item, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            OnBoardingItem onBoardingItem =
                    (OnBoardingItem) bundle.getSerializable(OnBoardingAdapter.ON_BOARDING_ITEM_KEY);
            mBinding.setOnBoardingItem(onBoardingItem);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
