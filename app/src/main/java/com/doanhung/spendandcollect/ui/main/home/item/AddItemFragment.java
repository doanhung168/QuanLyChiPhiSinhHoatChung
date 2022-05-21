package com.doanhung.spendandcollect.ui.main.home.item;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.doanhung.spendandcollect.R;
import com.doanhung.spendandcollect.databinding.FragmentAddItemBinding;
import com.doanhung.spendandcollect.ui.main.home.ItemViewModel;
import com.doanhung.spendandcollect.util.Util;
import com.doanhung.spendandcollect.util.event.CreateItemResultEvent;
import com.doanhung.spendandcollect.util.event.MessageEvent;
import com.doanhung.spendandcollect.util.event.Event;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddItemFragment extends Fragment {

    private FragmentAddItemBinding fragmentAddItemBinding;
    private NavController mainNavController;
    private ItemViewModel itemViewModel;

    private String current = "";


    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            startActivityForGettingImage();
                        } else {
                            Toast.makeText(requireContext(), "Permission deny!", Toast.LENGTH_LONG).show();
                        }
                    }
            );

    private final ActivityResultLauncher<Intent> getImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        itemViewModel.setItemImageUri(uri);
                        fragmentAddItemBinding.imvItemImage.setImageURI(uri);
                    } else {
                        fragmentAddItemBinding.imvItemImage.setImageDrawable(
                                getResources().getDrawable(R.drawable.ic_add_item)
                        );
                        itemViewModel.setItemImageUri(null);
                    }
                }
            }
    );


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainNavController = Navigation.findNavController(requireActivity(), R.id.mainNavHost);
        itemViewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentAddItemBinding = FragmentAddItemBinding.inflate(inflater, container, false);
        return fragmentAddItemBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentAddItemBinding.toolbar.setNavigationOnClickListener(v -> {
            mainNavController.popBackStack();
        });


        fragmentAddItemBinding.edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().equals(current) && !s.toString().equals("")) {
                    fragmentAddItemBinding.edtPrice.removeTextChangedListener(this);

                    Locale localeVN = new Locale("vi", "VN");
                    NumberFormat vn = NumberFormat.getInstance(localeVN);

                    String cleanString = s.toString().replaceAll("[.]", "");
                    Long number = Long.valueOf(cleanString);
                    String formatted = vn.format(number);

                    current = formatted;
                    fragmentAddItemBinding.edtPrice.setText(formatted);
                    fragmentAddItemBinding.edtPrice.setSelection(formatted.length());
                    fragmentAddItemBinding.edtPrice.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        itemViewModel.getSingleEvent().observe(getViewLifecycleOwner(), event -> {
            if (event.getName().equals(Event.CREATE_ITEM_RESULT_EVENT)) {
                if (((CreateItemResultEvent) event).getCreateItemResult().isSuccess()) {
                    mainNavController.popBackStack();
                }
                Toast.makeText(
                        requireContext(),
                        ((CreateItemResultEvent) event).getCreateItemResult().getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            } else if (event.getName().equals(Event.MESSAGE_EVENT)) {
                Toast.makeText(
                        requireContext(),
                        ((MessageEvent) event).getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        fragmentAddItemBinding.imvItemImage.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    startActivityForGettingImage();
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

            } else {
                startActivityForGettingImage();
            }
        });

        fragmentAddItemBinding.btnAddItem.setOnClickListener(v -> {
            String content = fragmentAddItemBinding.edtContent.getText().toString().trim();
            String price = fragmentAddItemBinding.edtPrice.getText().toString().trim();

            if (TextUtils.isEmpty(content)) {
                Toast.makeText(requireContext(), "Vui lòng nhập nội dung", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(price)) {
                Toast.makeText(requireContext(), "Vui lòng nhập giá", Toast.LENGTH_LONG).show();
                return;
            }

            RequestBody contentPart = RequestBody.create(content, MediaType.parse("text/plain"));
            RequestBody pricePart = RequestBody.create(price, MediaType.parse("text/plain"));
            RequestBody groupPart = RequestBody.create(
                    itemViewModel.getCurrentGroup().getValue().getId(),
                    MediaType.parse("text/plain")
            );

            MultipartBody.Part itemImagePart = null;
            if (itemViewModel.getItemImageUri() != null) {
                File file = new File(Util.getPathFromUri(requireContext(), itemViewModel.getItemImageUri()));

                RequestBody itemImageRequestBody = RequestBody.create(
                        file,
                        MediaType.parse("image/*")
                );

                itemImagePart = MultipartBody.Part.createFormData(
                        "image",
                        file.getName(),
                        itemImageRequestBody
                );
            }

            itemViewModel.createItem(itemImagePart, contentPart, pricePart, groupPart, Util.getToken(requireContext()));
        });

    }

    @Override
    public void onDestroyView() {
        fragmentAddItemBinding = null;
        super.onDestroyView();
    }

    private void startActivityForGettingImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        getImage.launch(intent);
    }
}
