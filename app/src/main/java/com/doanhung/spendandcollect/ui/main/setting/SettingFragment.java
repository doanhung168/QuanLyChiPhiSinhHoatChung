package com.doanhung.spendandcollect.ui.main.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.doanhung.spendandcollect.R;
import com.doanhung.spendandcollect.databinding.FragmentSettingBinding;
import com.doanhung.spendandcollect.ui.main.AuthViewModel;
import com.doanhung.spendandcollect.util.Util;
import com.doanhung.spendandcollect.util.event.Event;
import com.doanhung.spendandcollect.util.event.LoadingEvent;
import com.doanhung.spendandcollect.util.event.MessageEvent;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;
    private NavController navController;
    private NavController appNavController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        appNavController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
        AuthViewModel authViewModel = new ViewModelProvider(getParentFragment()).get(AuthViewModel.class);
        authViewModel.getAuth(Util.getToken(requireContext()));

        authViewModel.getAuth().observe(getViewLifecycleOwner(), user -> {
            binding.tvUserName.setText(user.getUserName());
            binding.tvUserId.setText("Id: " + user.getId());
            Glide.with(binding.imvUserAvatar)
                    .load(user.getAvatar())
                    .error(R.drawable.user)
                    .fitCenter()
                    .into(binding.imvUserAvatar);
        });


        authViewModel.getSingleEvent().observe(getViewLifecycleOwner(), event -> {
            if (event.getName().equals(Event.MESSAGE_EVENT)) {
                Toast.makeText(requireContext(), ((MessageEvent) event).getMessage(), Toast.LENGTH_SHORT).show();
            } else if (event.getName().equals(Event.LOADING_EVENT)) {
                if (((LoadingEvent) event).isLoading()) {
                    binding.root.setVisibility(View.GONE);
                    binding.shimmer.setVisibility(View.VISIBLE);
                } else {
                    binding.root.setVisibility(View.VISIBLE);
                    binding.shimmer.setVisibility(View.GONE);
                    binding.shimmer.stopShimmer();
                }
            }
        });

        binding.cvAddFriendRequest.setOnClickListener(v -> {
            navController.navigate(R.id.action_settingFragment_to_accessFriendFragment);
        });

        binding.cvSignOut.setOnClickListener(v -> {
            Util.setToken(requireContext(), "");
            Util.setAuthId(requireContext(), "");
            appNavController.navigate(R.id.action_global_signInFragment);
        });

        binding.cvChangePassword.setOnClickListener(v -> {

        });


    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}