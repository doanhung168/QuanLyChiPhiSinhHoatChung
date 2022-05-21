package com.doanhung.spendandcollect.util;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.doanhung.spendandcollect.data.model.remote.model.SearchUser;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DataBindingAdapters {

    @BindingAdapter("imageResource")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter(value = {"preView", "nextView"})
    public static void requestFocus(EditText view, EditText preView, EditText nextView) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (view.getText().length() == 1) {
                    if (nextView != null) {
                        view.clearFocus();
                        nextView.requestFocus();
                    }
                }
                if (view.getText().length() == 0) {
                    if (preView != null) {
                        view.clearFocus();
                        preView.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        view.addTextChangedListener(textWatcher);
    }

    @BindingAdapter({"imageUrl", "error"})
    public static void loadImage(ImageView imageView, String url, Drawable error) {
        if (url != null) {
            Glide.with(imageView.getContext())
                    .load(url)
                    .fitCenter()
                    .placeholder(error)
                    .into(imageView);
        }
    }

    @BindingAdapter("price")
    public static void setPrice(TextView textView, String value) {
        if (value != null) {
            Long number = Long.parseLong(value);
            Locale locale = new Locale("vi", "VN");
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
            textView.setText(currencyFormat.format(number));
        }
    }


    @BindingAdapter("time")
    public static void setTime(TextView textView, String value) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
        try {
            String finalStr = outputFormat.format(inputFormat.parse(value));
            System.out.println(finalStr);
            textView.setText(finalStr);
        } catch (ParseException e) {
            textView.setText("");
        }
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter({"auth", "searchUser"})
    public static void setStateFriend(TextView textView, SearchUser auth, SearchUser searchUser) {

        List<String> authFriends = auth.getFriendRequests();
        for(String userId: authFriends) {
            if(userId.equals(searchUser.getId())) {
                textView.setText("Đồng ý kết bạn");
                return;
            }
        }

        List<String> friendRequests = searchUser.getFriendRequests();
        for (String userId : friendRequests) {
            if (userId.equals(auth.getId())) {
                textView.setText("Hủy yêu cầu kết bạn");
                return;
            }
        }

        List<String> friends = searchUser.getFriends();
        for (String userId : friends) {
            if (userId.equals(auth.getId())) {
                textView.setText("Hủy bạn bè");
                return;
            }
        }

        textView.setText("Thêm bạn bè");
    }


}
