package com.doanhung.spendandcollect.ui.auth.sign_up;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.doanhung.spendandcollect.R;
import com.doanhung.spendandcollect.databinding.FragmentSignUpBinding;
import com.doanhung.spendandcollect.util.event.Event;
import com.doanhung.spendandcollect.util.event.SignUpDataEvent;

public class SignUpFragment extends Fragment {

    private static final String TAG = "SignUpFragment";
    private FragmentSignUpBinding fragmentSignUpBinding;
    private NavController navController;
    private SignUpViewModel signUpViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        signUpViewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSignUpBinding = FragmentSignUpBinding.inflate(inflater, container, false);

        fragmentSignUpBinding.setLifecycleOwner(getViewLifecycleOwner());
        fragmentSignUpBinding.setSignUpViewModel(signUpViewModel);
        fragmentSignUpBinding.setActivity(requireActivity());

        return fragmentSignUpBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signUpViewModel.getSingleEvent().observe(getViewLifecycleOwner(), event -> {

            if (event.getName().equals(Event.NAVIGATE_TO_VERIFY_FRAGMENT_EVENT)) {

                SignUpFragmentDirections.ActionSignUpFragmentToVerifyFragment action =
                        SignUpFragmentDirections.actionSignUpFragmentToVerifyFragment(
                                ((SignUpDataEvent) event).getSignUpData()
                        );

                navController.navigate(action);

            } else if (event.getName().equals(Event.NAVIGATE_TO_SIGN_IN_FRAGMENT_EVENT)) {

                navController.navigate(R.id.action_signUpFragment_to_signInFragment);

            }
        });
    }


    @Override
    public void onDestroyView() {
        fragmentSignUpBinding = null;
        super.onDestroyView();
    }

}