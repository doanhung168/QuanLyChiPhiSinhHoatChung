package com.doanhung.spendandcollect.ui.on_boarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.doanhung.spendandcollect.R;
import com.doanhung.spendandcollect.databinding.FragmentOnBoardingBinding;
import com.doanhung.spendandcollect.ui.main.HideBottomBarInterface;
import com.doanhung.spendandcollect.util.Util;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OnBoardingFragment extends Fragment {

    private FragmentOnBoardingBinding mBinding;
    private OnBoardingViewModel onBoardingViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBoardingViewModel =
                new ViewModelProvider(this, new OnBoardingViewModelFactory(requireActivity().getApplication()))
                        .get(OnBoardingViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentOnBoardingBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set up view
        setUpViewPager(mBinding.viewpager);
        setUpTabLayoutWithViewPager(mBinding.tabLayout, mBinding.viewpager);
        setUpBtnNext(mBinding.btnNext, mBinding.viewpager);
        setUpBtnSkip(mBinding.tvSkip, mBinding.viewpager);
        setUpBtnStart(mBinding.btnStart);

    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        super.onDestroyView();

    }


    private void setUpBtnStart(Button btnStart) {
        btnStart.setOnClickListener(v -> {
            Util.markOpenedFirstTimeApp(requireContext());
            NavHostFragment.findNavController(this).navigate(R.id.action_onBoardingFragment_to_signInFragment);
        });
    }

    private void setUpViewPager(ViewPager2 viewpager) {
        viewpager.setAdapter(onBoardingViewModel.createOnBoardingAdapter(this));
    }

    private void setUpBtnSkip(TextView tvSkip, ViewPager2 viewpager) {
        tvSkip.setOnClickListener(v -> {
            viewpager.setCurrentItem(viewpager.getAdapter().getItemCount() - 1);
        });
    }

    private void setUpBtnNext(ImageButton btnNext, ViewPager2 viewpager) {
        btnNext.setOnClickListener(v -> {
            viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
        });
    }

    private void setUpTabLayoutWithViewPager(TabLayout tabLayout, ViewPager2 viewPager) {
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                controller(tab.getPosition() == viewPager.getAdapter().getItemCount() - 1);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 0, 24, 0);
            tab.requestLayout();
        }
    }

    private void controller(boolean isLast) {
        if (isLast) {
            Animation animation =
                    AnimationUtils.loadAnimation(getContext(), R.anim.start_button_anim);

            mBinding.btnStart.setVisibility(View.VISIBLE);
            mBinding.btnStart.setAnimation(animation);

            mBinding.btnNext.setVisibility(View.INVISIBLE);
            mBinding.tabLayout.setVisibility(View.INVISIBLE);
            mBinding.tvSkip.setVisibility(View.INVISIBLE);
        } else {
            mBinding.btnStart.setVisibility(View.INVISIBLE);
            mBinding.btnNext.setVisibility(View.VISIBLE);
            mBinding.tabLayout.setVisibility(View.VISIBLE);
            mBinding.tvSkip.setVisibility(View.VISIBLE);
        }
    }


}