package com.doanhung.spendandcollect.ui.auth.sign_in;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.doanhung.spendandcollect.R;
import com.doanhung.spendandcollect.databinding.FragmentSignInBinding;
import com.doanhung.spendandcollect.util.Util;
import com.doanhung.spendandcollect.util.event.Event;
import com.doanhung.spendandcollect.util.event.FailAuthEvent;
import com.doanhung.spendandcollect.util.event.SignInResultEvent;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;


public class SignInFragment extends Fragment {

    private static final String TAG = "SignInFragment";
    private FragmentSignInBinding fragmentSignInBinding;
    private SignInViewModel signInViewModel;
    private NavController navController;

    private CallbackManager callbackManager;
    private GoogleSignInClient googleSignInClient;

    private final ActivityResultLauncher<Intent> getGoogleClientData =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            if (result.getData() == null) {
                                Toast.makeText(requireContext(), "Login With Google failure!", Toast.LENGTH_LONG).show();
                            } else {
                                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                                handleSignInResult(task);
                            }
                        }
                    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSignInBinding = FragmentSignInBinding.inflate(inflater, container, false);
        return fragmentSignInBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        navController = NavHostFragment.findNavController(this);

        fragmentSignInBinding.setLifecycleOwner(getViewLifecycleOwner());
        fragmentSignInBinding.setActivity(requireActivity());
        fragmentSignInBinding.setSignInViewModel(signInViewModel);

        fragmentSignInBinding.root.setVisibility(View.GONE);
        fragmentSignInBinding.loading.setVisibility(View.VISIBLE);

        if (Util.checkIsFirstOpenApp(requireContext())) {
            navController.navigate(R.id.action_signInFragment_to_onBoardingFragment);
        } else {

            String authToken = Util.getToken(requireContext());
            if (authToken.equals("")) {
                fragmentSignInBinding.root.setVisibility(View.VISIBLE);
                fragmentSignInBinding.loading.setVisibility(View.INVISIBLE);
            } else {
                signInViewModel.getAuth(authToken);
            }

        }

        setUpLoginWithFacebook(view);

        setUpLoginWithGoogle(requireActivity());

        signInViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            switch (event.getName()) {
                case Event.NAVIGATE_TO_SIGN_UP_FRAGMENT_EVENT:
                    navController.navigate(R.id.action_signInFragment_to_signUpFragment);
                    break;
                case Event.SIGN_IN_RESULT_EVENT:
                    Util.setToken(requireContext(), ((SignInResultEvent) event).getSignInResult().getToken());
                    String authToken = Util.getToken(requireContext());
                    signInViewModel.getAuth(authToken);
                    break;
                case Event.FAIL_AUTH_EVENT:
                    fragmentSignInBinding.root.setVisibility(View.VISIBLE);
                    fragmentSignInBinding.loading.setVisibility(View.INVISIBLE);
                    Toast.makeText(requireContext(), ((FailAuthEvent) event).getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        signInViewModel.getAuth().observe(getViewLifecycleOwner(), auth -> {
            Util.setAuthId(requireContext(), auth.getId());
            navController.navigate(R.id.action_signInFragment_to_MainFragment);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        fragmentSignInBinding = null;
        super.onDestroyView();
    }

    private void setUpLoginWithFacebook(View view) {

        fragmentSignInBinding.imvFacebook.setOnClickListener(v -> {
            LoginButton loginButton = view.findViewById(R.id.btnSignInWithFacebook);
            loginButton.performClick();
        });

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = view.findViewById(R.id.btnSignInWithFacebook);
        loginButton.setFragment(this);
        loginButton.setPermissions("public_profile", "user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "onSuccess: ");
                handleFacebookAccessToken(loginResult);
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(requireContext(), "Login With Facebook failure!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleFacebookAccessToken(LoginResult loginResult) {

        String accessToken = loginResult.getAccessToken().getToken();
        Log.e("accessToken", accessToken);

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                (object, response) -> {

                    String id = "";
                    String name = "";
                    String imageUrl = "";

                    try {
                        id = object.getString("id");
                        name = object.getString("name");
                        imageUrl = (String) object.getJSONObject("picture").getJSONObject("data").get("url");
                    } catch (JSONException e) {
                        Log.e(TAG, "handleFacebookAccessToken: " + e.getMessage());
                    }

                    Log.e(TAG, "handleFacebookAccessToken: " + id + " " + name + " " + imageUrl);
                    LoginManager.getInstance().logOut();
                    signInViewModel.signInWithFacebook(id, name, imageUrl);
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, picture.getType(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void setUpLoginWithGoogle(Activity activity) {

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(activity, options);

        fragmentSignInBinding.imvGoogle.setOnClickListener(v -> {
            Intent googleIntent = googleSignInClient.getSignInIntent();
            getGoogleClientData.launch(googleIntent);
        });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            String userName = "";
            String authGoogleID = "";
            String image = "";

            authGoogleID = account.getId();
            userName = account.getDisplayName();

            if (account.getId() != null) {
                authGoogleID = account.getId();
            }

            if (account.getDisplayName() != null) {
                userName = account.getDisplayName();
            }
            if (account.getPhotoUrl() != null) {
                image = String.valueOf(account.getPhotoUrl());
            }

            googleSignInClient.revokeAccess();
            signInViewModel.signInWithGoogle(authGoogleID, userName, image);
        } catch (ApiException e) {
            Toast.makeText(requireContext(), "Login With Google failure!", Toast.LENGTH_LONG).show();
            ;
        }
    }


}